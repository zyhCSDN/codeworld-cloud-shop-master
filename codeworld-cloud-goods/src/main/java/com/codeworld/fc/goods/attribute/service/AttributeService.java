package com.codeworld.fc.goods.attribute.service;

import com.codeworld.fc.common.response.DataResponse;
import com.codeworld.fc.common.response.FCResponse;
import com.codeworld.fc.goods.attribute.entity.Attribute;
import com.codeworld.fc.goods.attribute.request.AttributeAddRequest;
import com.codeworld.fc.goods.attribute.request.AttributeSearchRequest;
import com.codeworld.fc.goods.attribute.response.AttributeResponse;

import java.util.List;

public interface AttributeService {
    /**
     * 分页查询属性
     * @param attributeSearchRequest
     * @return
     */
    FCResponse<DataResponse<List<AttributeResponse>>> getPageAttribute(AttributeSearchRequest attributeSearchRequest);

    /**
     * 添加属性
     * @param attributeAddRequest
     * @return
     */
    FCResponse<Void> addAttribute(AttributeAddRequest attributeAddRequest);

    /**
     * 删除属性
     * @param id
     * @return
     */
    FCResponse<Void> delete(Long id);

    /**
     * 根据分配id查询属性（除开规格属性）
     * @param categoryId
     * @return
     */
    FCResponse<List<Attribute>> getAttributeByCategoryId(Long categoryId);
}
