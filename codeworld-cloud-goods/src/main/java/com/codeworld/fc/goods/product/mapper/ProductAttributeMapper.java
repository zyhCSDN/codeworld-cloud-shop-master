package com.codeworld.fc.goods.product.mapper;

import com.codeworld.fc.goods.product.entity.ProductAttribute;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ProductAttributeMapper {


    /**
     * 添加商品和属性之间关系
     * @param productAttribute
     */
    void add(ProductAttribute productAttribute);

    /**
     * 是否存在商品使用该属性
     * @param id
     * @return
     */
    Integer wheatherExist(Long id);
}
