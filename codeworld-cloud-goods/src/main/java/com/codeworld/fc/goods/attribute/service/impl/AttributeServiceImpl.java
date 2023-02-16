package com.codeworld.fc.goods.attribute.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codeworld.fc.common.base.PageQuery;
import com.codeworld.fc.common.enums.HttpFcStatus;
import com.codeworld.fc.common.enums.HttpMsg;
import com.codeworld.fc.common.exception.FCException;
import com.codeworld.fc.common.response.DataResponse;
import com.codeworld.fc.common.response.FCResponse;
import com.codeworld.fc.common.utils.IDGeneratorUtil;
import com.codeworld.fc.goods.attribute.entity.Attribute;
import com.codeworld.fc.goods.attribute.entity.CategoryAttribute;
import com.codeworld.fc.goods.attribute.mapper.AttributeMapper;
import com.codeworld.fc.goods.attribute.mapper.CategoryAttributeMapper;
import com.codeworld.fc.goods.attribute.request.AttributeAddRequest;
import com.codeworld.fc.goods.attribute.request.AttributeSearchRequest;
import com.codeworld.fc.goods.attribute.response.AttributeResponse;
import com.codeworld.fc.goods.attribute.service.AttributeService;
import com.codeworld.fc.goods.param.mapper.ParamMapper;
import com.codeworld.fc.goods.product.mapper.ProductAttributeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName AttributeServiceImpl
 * Description TODO
 * Author Lenovo
 * Date 2020/12/4
 * Version 1.0
 **/
@Service
public class AttributeServiceImpl implements AttributeService {

    @Autowired(required = false)
    private AttributeMapper attributeMapper;
    @Autowired
    private CategoryAttributeMapper categoryAttributeMapper;
    @Autowired(required = false)
    private ProductAttributeMapper productAttributeMapper;
    @Autowired(required = false)
    private ParamMapper paramMapper;

    /**
     * 分页查询属性
     *
     * @param attributeSearchRequest
     * @return
     */
    @Override
    public FCResponse<DataResponse<List<AttributeResponse>>> getPageAttribute(AttributeSearchRequest attributeSearchRequest) {
        PageHelper.startPage(attributeSearchRequest.getPage(), attributeSearchRequest.getLimit());
        List<AttributeResponse> attributeResponses = this.attributeMapper.getPageAttribute(attributeSearchRequest);
        if (CollectionUtils.isEmpty(attributeResponses)) {
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.attribute.ATTRIBUTE_DATA_EMPTY.getMsg(), DataResponse.dataResponse(attributeResponses, 0L));
        }
        PageInfo<AttributeResponse> pageInfo = new PageInfo<>(attributeResponses);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.attribute.ATTRIBUTE_GET_SUCCESS.getMsg(), DataResponse.dataResponse(pageInfo.getList(), pageInfo.getTotal()));
    }

    /**
     * 添加属性
     *
     * @param attributeAddRequest
     * @return
     */
    @Override
    @Transactional
    public FCResponse<Void> addAttribute(AttributeAddRequest attributeAddRequest) {
        try {
            Attribute attribute = new Attribute();
            BeanUtil.copyProperties(attributeAddRequest, attribute);
            attribute.setId(IDGeneratorUtil.getNextId());
            attribute.setCreateTime(new Date());
            attribute.setUpdateTime(attribute.getCreateTime());
            this.attributeMapper.addAttribute(attribute);
            CategoryAttribute categoryAttribute = new CategoryAttribute();
            categoryAttribute.setId(IDGeneratorUtil.getNextId());
            categoryAttribute.setCategoryId(attributeAddRequest.getCategoryId());
            categoryAttribute.setAttributeId(attribute.getId());
            categoryAttribute.setCreateTime(new Date());
            categoryAttribute.setUpdateTime(categoryAttribute.getCreateTime());
            this.categoryAttributeMapper.addCategoryAttribute(categoryAttribute);
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.attribute.ATTRIBUTE_ADD_SUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("系统错误");
        }
    }

    /**
     * 删除属性
     *
     * @param id
     * @return
     */
    @Override
    public FCResponse<Void> delete(Long id) {
        // 查询属性是否有被商品使用
        Integer count = this.productAttributeMapper.wheatherExist(id);
        if (count != 0) {
            throw new FCException("存在商品使用属性，不能删除");
        }
        // 先删除分类和属性之间关系
        this.categoryAttributeMapper.deleteByAttributeId(id);
        // 再删除属性和参数之间的关系
        List<Long> attributeIds = new ArrayList<>();
        attributeIds.add(id);
        this.paramMapper.deleteParamByAttributeIds(attributeIds);
        // 最后删除属性
        this.attributeMapper.delete(id);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), "删除成功");
    }

    /**
     * 根据分配id查询属性（除开规格属性）
     *
     * @param categoryId
     * @return
     */
    @Override
    public FCResponse<List<Attribute>> getAttributeByCategoryId(Long categoryId) {
        List<Attribute> attributes = this.attributeMapper.getAttributeByCategoryId(categoryId);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), "查询成功", attributes);
    }
}
