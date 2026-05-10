package com.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.dto.StockInDTO;
import com.mall.dto.StockLockDTO;
import com.mall.dto.StockOutDTO;
import com.mall.entity.ProductSku;
import com.mall.entity.StockRecord;
import com.mall.mapper.ProductSkuMapper;
import com.mall.mapper.StockRecordMapper;
import com.mall.service.StockService;
import com.mall.utils.DistributedLockUtil;
import com.mall.utils.RedisUtil;
import com.mall.vo.StockRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl extends ServiceImpl<StockRecordMapper, StockRecord> implements StockService {

    private static final String LOCKED_STOCK_KEY = "locked:stock:";
    private static final int STOCK_IN = 1;
    private static final int STOCK_OUT = 2;
    private static final int STOCK_LOCK = 3;
    private static final int STOCK_UNLOCK = 4;
    private static final int STOCK_DEDUCT = 5;

    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private StockRecordMapper stockRecordMapper;

    @Autowired
    private DistributedLockUtil lockUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    @Transactional
    public void stockIn(StockInDTO dto) {
        String lockKey = "stock:in:" + dto.getSkuId();
        try {
            if (lockUtil.tryLock(lockKey, 5, 30)) {
                ProductSku sku = productSkuMapper.selectById(dto.getSkuId());
                if (sku == null) {
                    throw new RuntimeException("SKU不存在");
                }

                Integer beforeQuantity = sku.getStock();
                Integer afterQuantity = beforeQuantity + dto.getQuantity();

                sku.setStock(afterQuantity);
                productSkuMapper.updateById(sku);

                saveStockRecord(dto.getSkuId(), STOCK_IN, dto.getQuantity(), beforeQuantity, afterQuantity, null, dto.getRemark());
            } else {
                throw new RuntimeException("获取锁失败，请稍后重试");
            }
        } finally {
            lockUtil.unlock(lockKey);
        }
    }

    @Override
    @Transactional
    public void stockOut(StockOutDTO dto) {
        String lockKey = "stock:out:" + dto.getSkuId();
        try {
            if (lockUtil.tryLock(lockKey, 5, 30)) {
                ProductSku sku = productSkuMapper.selectById(dto.getSkuId());
                if (sku == null) {
                    throw new RuntimeException("SKU不存在");
                }

                Integer beforeQuantity = sku.getStock();
                if (beforeQuantity < dto.getQuantity()) {
                    throw new RuntimeException("库存不足");
                }

                Integer afterQuantity = beforeQuantity - dto.getQuantity();

                sku.setStock(afterQuantity);
                productSkuMapper.updateById(sku);

                saveStockRecord(dto.getSkuId(), STOCK_OUT, dto.getQuantity(), beforeQuantity, afterQuantity, dto.getOrderNo(), dto.getRemark());
            } else {
                throw new RuntimeException("获取锁失败，请稍后重试");
            }
        } finally {
            lockUtil.unlock(lockKey);
        }
    }

    @Override
    @Transactional
    public void lockStock(StockLockDTO dto) {
        for (StockLockDTO.SkuStockItem item : dto.getSkuList()) {
            String lockKey = "stock:lock:" + item.getSkuId();
            try {
                if (lockUtil.tryLock(lockKey, 5, 30)) {
                    ProductSku sku = productSkuMapper.selectById(item.getSkuId());
                    if (sku == null) {
                        throw new RuntimeException("SKU不存在: " + item.getSkuId());
                    }

                    Integer availableStock = getAvailableStock(sku.getId(), sku.getStock());
                    if (availableStock < item.getQuantity()) {
                        throw new RuntimeException("库存不足，SKU: " + sku.getSkuCode() + "，可用库存: " + availableStock);
                    }

                    Integer lockedStock = getLockedStock(sku.getId());
                    Integer newLockedStock = lockedStock + item.getQuantity();
                    redisUtil.set(LOCKED_STOCK_KEY + sku.getId(), String.valueOf(newLockedStock), 3600, TimeUnit.SECONDS);

                    saveStockRecord(sku.getId(), STOCK_LOCK, item.getQuantity(), sku.getStock(), sku.getStock(), dto.getOrderNo(), "订单锁定");
                } else {
                    throw new RuntimeException("获取锁失败，请稍后重试");
                }
            } finally {
                lockUtil.unlock(lockKey);
            }
        }
    }

    @Override
    @Transactional
    public void unlockStock(String orderNo) {
        LambdaQueryWrapper<StockRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockRecord::getOrderNo, orderNo);
        wrapper.eq(StockRecord::getType, STOCK_LOCK);
        List<StockRecord> lockRecords = stockRecordMapper.selectList(wrapper);

        for (StockRecord record : lockRecords) {
            String lockKey = "stock:unlock:" + record.getSkuId();
            try {
                if (lockUtil.tryLock(lockKey, 5, 30)) {
                    Integer lockedStock = getLockedStock(record.getSkuId());
                    Integer newLockedStock = lockedStock - record.getQuantity();
                    if (newLockedStock <= 0) {
                        redisUtil.delete(LOCKED_STOCK_KEY + record.getSkuId());
                    } else {
                        redisUtil.set(LOCKED_STOCK_KEY + record.getSkuId(), String.valueOf(newLockedStock), 3600, TimeUnit.SECONDS);
                    }

                    ProductSku sku = productSkuMapper.selectById(record.getSkuId());
                    if (sku != null) {
                        saveStockRecord(record.getSkuId(), STOCK_UNLOCK, record.getQuantity(), sku.getStock(), sku.getStock(), orderNo, "订单取消解锁");
                    }
                } else {
                    throw new RuntimeException("获取锁失败，请稍后重试");
                }
            } finally {
                lockUtil.unlock(lockKey);
            }
        }
    }

    @Override
    @Transactional
    public void deductStock(String orderNo) {
        LambdaQueryWrapper<StockRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StockRecord::getOrderNo, orderNo);
        wrapper.eq(StockRecord::getType, STOCK_LOCK);
        List<StockRecord> lockRecords = stockRecordMapper.selectList(wrapper);

        for (StockRecord record : lockRecords) {
            String lockKey = "stock:deduct:" + record.getSkuId();
            try {
                if (lockUtil.tryLock(lockKey, 5, 30)) {
                    ProductSku sku = productSkuMapper.selectById(record.getSkuId());
                    if (sku == null) {
                        throw new RuntimeException("SKU不存在");
                    }

                    Integer beforeQuantity = sku.getStock();
                    Integer afterQuantity = beforeQuantity - record.getQuantity();
                    if (afterQuantity < 0) {
                        throw new RuntimeException("库存不足");
                    }

                    sku.setStock(afterQuantity);
                    productSkuMapper.updateById(sku);

                    Integer lockedStock = getLockedStock(record.getSkuId());
                    Integer newLockedStock = lockedStock - record.getQuantity();
                    if (newLockedStock <= 0) {
                        redisUtil.delete(LOCKED_STOCK_KEY + record.getSkuId());
                    } else {
                        redisUtil.set(LOCKED_STOCK_KEY + record.getSkuId(), String.valueOf(newLockedStock), 3600, TimeUnit.SECONDS);
                    }

                    saveStockRecord(record.getSkuId(), STOCK_DEDUCT, record.getQuantity(), beforeQuantity, afterQuantity, orderNo, "订单扣减");
                } else {
                    throw new RuntimeException("获取锁失败，请稍后重试");
                }
            } finally {
                lockUtil.unlock(lockKey);
            }
        }
    }

    @Override
    public Integer getStock(Long skuId) {
        ProductSku sku = productSkuMapper.selectById(skuId);
        if (sku == null) {
            throw new RuntimeException("SKU不存在");
        }
        return sku.getStock();
    }

    @Override
    public List<StockRecordVO> getStockRecords(Long skuId, Integer type, String orderNo) {
        LambdaQueryWrapper<StockRecord> wrapper = new LambdaQueryWrapper<>();
        if (skuId != null) {
            wrapper.eq(StockRecord::getSkuId, skuId);
        }
        if (type != null) {
            wrapper.eq(StockRecord::getType, type);
        }
        if (orderNo != null) {
            wrapper.eq(StockRecord::getOrderNo, orderNo);
        }
        wrapper.orderByDesc(StockRecord::getCreateTime);

        List<StockRecord> records = stockRecordMapper.selectList(wrapper);
        return records.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    private void saveStockRecord(Long skuId, Integer type, Integer quantity, Integer beforeQuantity, Integer afterQuantity, String orderNo, String remark) {
        StockRecord record = new StockRecord();
        record.setSkuId(skuId);
        record.setType(type);
        record.setQuantity(quantity);
        record.setBeforeQuantity(beforeQuantity);
        record.setAfterQuantity(afterQuantity);
        record.setOrderNo(orderNo);
        record.setRemark(remark);
        stockRecordMapper.insert(record);
    }

    private Integer getLockedStock(Long skuId) {
        Object lockedStock = redisUtil.get(LOCKED_STOCK_KEY + skuId);
        return lockedStock != null ? Integer.parseInt(lockedStock.toString()) : 0;
    }

    private Integer getAvailableStock(Long skuId, Integer totalStock) {
        Integer lockedStock = getLockedStock(skuId);
        return totalStock - lockedStock;
    }

    private StockRecordVO convertToVO(StockRecord record) {
        StockRecordVO vo = new StockRecordVO();
        vo.setId(record.getId());
        vo.setSkuId(record.getSkuId());
        vo.setType(record.getType());
        vo.setTypeName(getTypeName(record.getType()));
        vo.setQuantity(record.getQuantity());
        vo.setBeforeQuantity(record.getBeforeQuantity());
        vo.setAfterQuantity(record.getAfterQuantity());
        vo.setOrderNo(record.getOrderNo());
        vo.setRemark(record.getRemark());
        vo.setCreateTime(record.getCreateTime());

        ProductSku sku = productSkuMapper.selectById(record.getSkuId());
        if (sku != null) {
            vo.setSkuCode(sku.getSkuCode());
        }

        return vo;
    }

    private String getTypeName(Integer type) {
        switch (type) {
            case STOCK_IN:
                return "入库";
            case STOCK_OUT:
                return "出库";
            case STOCK_LOCK:
                return "锁定";
            case STOCK_UNLOCK:
                return "解锁";
            case STOCK_DEDUCT:
                return "扣减";
            default:
                return "未知";
        }
    }
}
