package com.mall.service;

import com.mall.dto.ProductSkuCreateDTO;
import com.mall.dto.ProductSkuUpdateDTO;
import com.mall.vo.ProductSkuVO;

import java.util.List;

public interface ProductSkuService {

    Long createSku(ProductSkuCreateDTO dto);

    void updateSku(ProductSkuUpdateDTO dto);

    void deleteSku(Long id);

    ProductSkuVO getSkuById(Long id);

    List<ProductSkuVO> getSkuBySpuId(Long spuId);

    void updateSkuStatus(Long id, Integer status);
}
