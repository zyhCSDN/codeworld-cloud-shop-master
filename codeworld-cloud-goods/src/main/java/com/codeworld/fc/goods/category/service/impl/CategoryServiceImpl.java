package com.codeworld.fc.goods.category.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.codeworld.fc.common.enums.HttpFcStatus;
import com.codeworld.fc.common.enums.HttpMsg;
import com.codeworld.fc.common.exception.FCException;
import com.codeworld.fc.common.response.FCResponse;
import com.codeworld.fc.common.utils.IDGeneratorUtil;
import com.codeworld.fc.common.utils.JsonUtils;
import com.codeworld.fc.goods.attribute.entity.Attribute;
import com.codeworld.fc.goods.attribute.mapper.AttributeMapper;
import com.codeworld.fc.goods.category.domain.TreeBuilder;
import com.codeworld.fc.goods.category.domain.CategoryTreeNode;
import com.codeworld.fc.goods.category.entity.Category;
import com.codeworld.fc.goods.category.mapper.CategoryMapper;
import com.codeworld.fc.goods.category.request.CategoryRequest;
import com.codeworld.fc.goods.category.service.CategoryService;
import com.codeworld.fc.goods.param.mapper.ParamMapper;
import com.codeworld.fc.goods.product.mapper.ProductMapper;
import com.google.common.collect.Lists;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName CategoryServiceImpl
 * Description TODO
 * Author Lenovo
 * Date 2020/11/27
 * Version 1.0
 **/
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired(required = false)
    private CategoryMapper categoryMapper;
    @Autowired(required = false)
    private AttributeMapper attributeMapper;
    @Autowired(required = false)
    private ParamMapper paramMapper;
    @Autowired(required = false)
    private ProductMapper productMapper;

    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;
    private final String CATEGORY_LIST = "category_list";

    /**
     * ??????????????????
     *
     * @return
     */
    public FCResponse<Object> treeCategory() {
        List<Category> categories = this.categoryMapper.getAllCategory();
        final List<CategoryTreeNode> categoryTreeNodes = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(categories)) {
            categories.forEach(category -> {
                CategoryTreeNode categoryTreeNode = new CategoryTreeNode();
                BeanUtils.copyProperties(category, categoryTreeNode);
                categoryTreeNodes.add(categoryTreeNode);
            });
        }
        List<CategoryTreeNode> newCategoryTreeNodeList = TreeBuilder.buildCategoryTree(categoryTreeNodes);
        newCategoryTreeNodeList.stream().sorted(Comparator.comparing(CategoryTreeNode::getSortNo)).collect(Collectors.toList());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("categoryList", categories);
        jsonObject.put("categoryTree", newCategoryTreeNodeList);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.menu.MENU_GET_SUCCESS.getMsg(), jsonObject);
    }

    /**
     * ????????????
     *
     * @param categoryRequest
     * @return
     */
    @Override
    public FCResponse<Void> addCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        BeanUtil.copyProperties(categoryRequest, category);
        category.setId(IDGeneratorUtil.getNextId());
        category.setCreateTime(new Date());
        category.setUpdateTime(category.getCreateTime());
        this.categoryMapper.addCategory(category);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.category.CATEGORY_ADD_SUCCESS.getMsg());
    }

    /**
     * ????????????
     *
     * @param categoryRequest
     * @return
     */
    @Override
    public FCResponse<Void> updateCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        BeanUtil.copyProperties(categoryRequest, category);
        category.setUpdateTime(new Date());
        this.categoryMapper.updateCategory(category);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.category.CATEGORY_UPDATE_SUCCESS.getMsg());
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @Override
    public FCResponse<List<Category>> getAllCategory() {
        try {
            // ??????redis?????????
            if (this.stringRedisTemplate.hasKey(CATEGORY_LIST)) {
                String categoryJson = this.stringRedisTemplate.opsForValue().get(CATEGORY_LIST);
                List<Category> categories = JsonUtils.parseList(categoryJson, Category.class);
                return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.category.CATEGORY_GET_SUCCESS.getMsg(), categories);
            }
            // ??????????????????????????????
            List<Category> categories = this.categoryMapper.getAllCategory();
            if (CollectionUtils.isEmpty(categories)) {
                return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.category.CATEGORY_DATA_EMPTY.getMsg(), null);
            }
            // ??????????????????Json?????????redis???
            String categoryJson = JsonUtils.serialize(categories);
            this.stringRedisTemplate.opsForValue().set(CATEGORY_LIST, categoryJson);
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.category.CATEGORY_GET_SUCCESS.getMsg(), categories);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }


    /**
     * ????????????
     *
     * @param categoryId
     * @return
     */
    @Override
    public FCResponse<Void> deleteCategory(Long categoryId) {
        if (ObjectUtil.isEmpty(categoryId) || categoryId <= 0) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.category.CATEGORY_ID_ERROR.getMsg());
        }
        // ???????????????id?????????????????????????????????
        Integer count = this.productMapper.getBindProductByCategoryId(categoryId);
        if (count != 0) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEXIST.getCode(), HttpMsg.category.CATEGORY_BIND_PRODUCT.getMsg());
        }
        // count == 0????????????????????????
        // ????????????id???????????????????????????
        List<Category> categories = this.categoryMapper.getChildCategoryById(categoryId);
        if (!CollectionUtils.isEmpty(categories)) {
            categories.forEach(category -> {
                // ????????????id????????????
                List<Long> attributeIds = this.attributeMapper.getAttributeIdsByCategoryId(category.getId());
                if (!CollectionUtils.isEmpty(attributeIds)) {
                    // ?????????????????????????????????
                    this.paramMapper.deleteParamByAttributeIds(attributeIds);
                }
            });
        }
        //TODO
        return null;
    }

    /**
     * ????????????id??????????????????
     *
     * @param categoryId
     * @return
     */
    @Override
    public FCResponse<Category> getCategoryById(Long categoryId) {
        if (ObjectUtil.isEmpty(categoryId) || categoryId <= 0) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.category.CATEGORY_ID_ERROR.getMsg());
        }
        Category category = this.categoryMapper.getCategoryById(categoryId);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.category.CATEGORY_GET_SUCCESS.getMsg(), category);
    }

    /**
     * ??????????????????-app
     *
     * @return
     */
    @Override
    public FCResponse<List<Category>> getParentCategory() {
        // ???Redis?????????
        if (this.stringRedisTemplate.hasKey(CATEGORY_LIST)){
            String json = this.stringRedisTemplate.opsForValue().get(CATEGORY_LIST);
            assert json != null;
            List<Category> categories = JsonUtils.parseList(json, Category.class);
            // ??????parentId???0???????????????
            categories =categories.stream().filter(category -> {
                 return category.getParentId() == 0L;
            }).collect(Collectors.toList());
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(),HttpMsg.category.CATEGORY_GET_SUCCESS.getMsg(),categories);
        }
        return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(),HttpMsg.category.CATEGORY_DATA_EMPTY.getMsg());
    }
}
