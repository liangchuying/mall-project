package com.mall.service;

import com.mall.dto.*;
import com.mall.vo.StockRecordVO;

import java.util.List;

public interface StockService {

    void stockIn(StockInDTO dto);

    void stockOut(StockOutDTO dto);

    void lockStock(StockLockDTO dto);

    void unlockStock(String orderNo);

    void deductStock(String orderNo);

    Integer getStock(Long skuId);

    List<StockRecordVO> getStockRecords(Long skuId, Integer type, String orderNo);
}
