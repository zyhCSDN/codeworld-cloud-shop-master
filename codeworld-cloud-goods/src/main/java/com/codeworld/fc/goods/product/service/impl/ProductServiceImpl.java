package com.codeworld.fc.goods.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.codeworld.fc.common.domain.LoginInfoData;
import com.codeworld.fc.common.enums.HttpFcStatus;
import com.codeworld.fc.common.enums.HttpMsg;
import com.codeworld.fc.common.exception.FCException;
import com.codeworld.fc.common.response.DataResponse;
import com.codeworld.fc.common.response.FCResponse;
import com.codeworld.fc.common.utils.IDGeneratorUtil;
import com.codeworld.fc.common.utils.JsonUtils;
import com.codeworld.fc.goods.attribute.mapper.AttributeMapper;
import com.codeworld.fc.goods.client.MerchantClient;
import com.codeworld.fc.goods.client.SearchClient;
import com.codeworld.fc.goods.client.UserClient;
import com.codeworld.fc.goods.domain.MerchantResponse;
import com.codeworld.fc.goods.domain.Role;
import com.codeworld.fc.goods.domain.User;
import com.codeworld.fc.goods.interceptor.AuthInterceptor;
import com.codeworld.fc.goods.param.entity.Param;
import com.codeworld.fc.goods.param.mapper.ParamMapper;
import com.codeworld.fc.goods.param.response.ParamResponse;
import com.codeworld.fc.goods.product.domain.ElProductStatusDTO;
import com.codeworld.fc.goods.product.domain.ElSearchItem;
import com.codeworld.fc.goods.product.entity.Product;
import com.codeworld.fc.goods.product.entity.ProductAttribute;
import com.codeworld.fc.goods.product.entity.ProductDetail;
import com.codeworld.fc.goods.product.entity.ProductSku;
import com.codeworld.fc.goods.product.mapper.ProductAttributeMapper;
import com.codeworld.fc.goods.product.mapper.ProductDetailMapper;
import com.codeworld.fc.goods.product.mapper.ProductMapper;
import com.codeworld.fc.goods.product.mapper.ProductSkuMapper;
import com.codeworld.fc.goods.product.request.ExamineProductRequest;
import com.codeworld.fc.goods.product.request.ProductAddRequest;
import com.codeworld.fc.goods.product.request.ProductSearchRequest;
import com.codeworld.fc.goods.product.response.ProductResponse;
import com.codeworld.fc.goods.product.service.ProductService;
import com.codeworld.fc.goods.product.vo.SkuVO;
import com.codeworld.fc.goods.stock.entity.Stock;
import com.codeworld.fc.goods.stock.mapper.StockMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassName ProductServiceImpl
 * Description TODO
 * Author Lenovo
 * Date 2020/11/28
 * Version 1.0
 **/
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired(required = false)
    private ProductMapper productMapper;
    @Autowired(required = false)
    private ProductDetailMapper productDetailMapper;
    @Autowired(required = false)
    private ProductSkuMapper productSkuMapper;
    @Autowired(required = false)
    private StockMapper stockMapper;
    @Autowired(required = false)
    private ParamMapper paramMapper;
    @Autowired(required = false)
    private AttributeMapper attributeMapper;
    @Autowired(required = false)
    private ProductAttributeMapper productAttributeMapper;

    @Autowired(required = false)
    private AmqpTemplate amqpTemplate;

    @Autowired(required = false)
    private SearchClient searchClient;
    @Autowired
    private MerchantClient merchantClient;
    @Autowired
    private UserClient userClient;


    /**
     * ????????????????????????
     *
     * @param productSearchRequest
     * @return
     */
    @Override
    public FCResponse<DataResponse<List<ProductResponse>>> getPageProduct(ProductSearchRequest productSearchRequest) {
        LoginInfoData loginInfoData = AuthInterceptor.getLoginMember();
        if (ObjectUtils.isEmpty(loginInfoData)) {
            throw new FCException("??????????????????????????????");
        }
        // ????????????????????????--????????????
        if ("merchant".equals(loginInfoData.getResources())) {
            productSearchRequest.setMerchantId(loginInfoData.getId());
        } else {
            // ????????????
            productSearchRequest.setMerchantId(null);
            // ??????????????????
            FCResponse<User> response = this.userClient.getUserById(loginInfoData.getId());
            if (!response.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
                throw new FCException(response.getMsg());
            }
            // ??????????????????
            User user = response.getData();
            // ??????????????????????????????
            Role role = user.getRole();
//            if ()
        }
        PageHelper.startPage(productSearchRequest.getPage(), productSearchRequest.getLimit());
        List<ProductResponse> productResponses = this.productMapper.getPageProduct(productSearchRequest);
        if (CollectionUtils.isEmpty(productResponses)) {
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_DATA_EMPTY.getMsg(), DataResponse.dataResponse(productResponses, 0L));
        }
        PageInfo<ProductResponse> pageInfo = new PageInfo<>(productResponses);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_GET_SUCCESS.getMsg(), DataResponse.dataResponse(pageInfo.getList(), pageInfo.getTotal()));
    }

    /**
     * ??????????????????
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public FCResponse<Void> updateProductStatus(Long id, Integer status) {
        if (ObjectUtils.isEmpty(id) || id <= 0) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.product.PRODUCT_ID_ERROR.getMsg());
        }
        Product product = new Product();
        product.setId(id);
        product.setSaleAble(status);
        product.setUpdateTime(new Date());
        this.productMapper.updateProductStatus(product);
        ElProductStatusDTO elProductStatusDTO = new ElProductStatusDTO();
        elProductStatusDTO.setProductId(product.getId());
        elProductStatusDTO.setSaleAble(product.getSaleAble());
        String json = JsonUtils.serialize(elProductStatusDTO);
        // ????????????ElasticSearch???????????????
        this.amqpTemplate.convertAndSend("el.product.update.saleable", json);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_UPDATE_STATUS.getMsg());
    }

    /**
     * ????????????
     *
     * @param productAddRequest
     * @return
     */
    @Override
    @Transactional
    public FCResponse<Void> addProduct(ProductAddRequest productAddRequest) {
        try {
            // ????????????????????????
            LoginInfoData loginInfoData = AuthInterceptor.getLoginMember();
            if (ObjectUtils.isEmpty(loginInfoData)) {
                throw new FCException("??????????????????????????????");
            }
            Product product = new Product();
            BeanUtil.copyProperties(productAddRequest, product);
            product.setId(IDGeneratorUtil.getNextId());
            // ???????????????
            product.setSaleAble(0);
            product.setCreateTime(new Date());
            product.setUpdateTime(product.getCreateTime());
            product.setMerchantId(loginInfoData.getId());
            // ??????????????????
            product.setApproveStatus(-1);
            this.productMapper.addProduct(product);
            // ????????????????????????
            ProductDetail productDetail = new ProductDetail();
            productDetail.setId(product.getId());
            productDetail.setDesc(productAddRequest.getDesc());
            // ??????????????????
            productDetail.setGenericParam(productAddRequest.getGenericParam());
            productDetail.setSpecialParam(productAddRequest.getSpecialParam());
            productDetail.setAfterService(productAddRequest.getAfterService());
            productDetail.setPackingList(productAddRequest.getPackingList());
            productDetail.setCreateTime(new Date());
            productDetail.setUpdateTime(productDetail.getCreateTime());
            this.productDetailMapper.addProductDetail(productDetail);
            // ????????????????????????????????????
            Map<String, Object> productAttributeMap = BeanUtil.beanToMap(productAddRequest.getGenericParam());
            // ????????????id
            Set<String> attributeIds = productAttributeMap.keySet();
            for (String attributeId : attributeIds
            ) {
                ProductAttribute productAttribute = new ProductAttribute();
                productAttribute.setProductId(product.getId());
                productAttribute.setAttributeId(Long.parseLong(attributeId));
                this.productAttributeMapper.add(productAttribute);
            }
            // ??????Sku??????
            // ???Json?????????????????????List??????
            List<SkuVO> skuVOS = JsonUtils.parseList(productAddRequest.getSku(), SkuVO.class);
            if (CollectionUtils.isEmpty(skuVOS)) {
                return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.product.PRODUCT_PARAM_ERROR.getMsg());
            }
            // ??????????????????
            skuVOS.forEach(skuVO -> {
                StringBuilder stringBuilder = new StringBuilder();
                ProductSku productSku = new ProductSku();
                productSku.setId(IDGeneratorUtil.getNextId());
                productSku.setProductId(product.getId());
                stringBuilder.append(product.getTitle()).append(" ").append(skuVO.getColor()).append(" ").append(skuVO.getSpec());
                String title = stringBuilder.toString();
                productSku.setTitle(title);
                productSku.setPrice(skuVO.getPrice());
                productSku.setOwnSpec(skuVO.getOwnSpec());
                productSku.setImages(StringUtils.join(skuVO.getImages(), ","));
                productSku.setCreateTime(new Date());
                productSku.setUpdateTime(productSku.getCreateTime());
                // ??????sku
                this.productSkuMapper.addProductSku(productSku);
                // ???????????????
                Stock stock = new Stock();
                stock.setProductSkuId(productSku.getId());
                stock.setStock(skuVO.getStock());
                this.stockMapper.addStock(stock);
            });
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_ADD_SUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ????????????id???????????????Sku
     *
     * @param productId
     * @return
     */
    @Override
    public FCResponse<List<ProductSku>> getProductSkuByProductId(Long productId) {

        List<ProductSku> productSkus = this.productSkuMapper.getProductSkuByProductId(productId);
        if (CollectionUtils.isEmpty(productSkus)) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.product.PRODUCT_DATA_EMPTY.getMsg(), productSkus);
        }
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_GET_SUCCESS.getMsg(), productSkus);
    }

    /**
     * ????????????id??????????????????
     *
     * @param productId
     * @return
     */
    @Override
    public FCResponse<ProductDetail> getProductDetailByProductId(Long productId) {
        ProductDetail productDetail = this.productDetailMapper.getProductDetailByProductId(productId);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_GET_SUCCESS.getMsg(), productDetail);
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @Override
    public FCResponse<List<ProductResponse>> getNewProduct() {
        List<ProductResponse> productResponses = this.productMapper.getNewProduct();
        productResponses = productResponses.stream().map(productResponse -> {
            if (StringUtils.isNotBlank(productResponse.getImage())) {
                List<String> images = Arrays.asList(StringUtils.split(productResponse.getImage(), ","));
                productResponse.setImages(images);
            }
            productResponse.setImage(null);
            return productResponse;
        }).collect(Collectors.toList());
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_GET_SUCCESS.getMsg(), productResponses);
    }

    /**
     * ????????????????????????
     *
     * @param productSearchRequest
     * @return
     */
    @Override
    public FCResponse<DataResponse<List<ProductResponse>>> getPageProductTime(ProductSearchRequest productSearchRequest) {
        // ?????????????????????????????????????????????
        productSearchRequest.setMerchantId(null);
        PageHelper.startPage(productSearchRequest.getPage(), productSearchRequest.getLimit());
        List<ProductResponse> productResponses = this.productMapper.getPageProduct(productSearchRequest);
        if (CollectionUtils.isEmpty(productResponses)) {
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_DATA_EMPTY.getMsg(), DataResponse.dataResponse(productResponses, 0L));
        }
        PageInfo<ProductResponse> pageInfo = new PageInfo<>(productResponses);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_GET_SUCCESS.getMsg(), DataResponse.dataResponse(pageInfo.getList(), pageInfo.getTotal()));
    }

    /**
     * ????????????
     *
     * @param id
     * @return
     */
    @Override
    public FCResponse<Void> deleteGoods(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            return FCResponse.validateErrorResponse("??????ID??????");
        }
        // ??????????????????
        Product product = this.productMapper.getProductById(id);
        if (ObjectUtils.isEmpty(product)) {
            return FCResponse.validateErrorResponse("???????????????");
        }
        // ???????????????????????????
        if (product.getSaleAble() == -1) {
            return FCResponse.validateErrorResponse("???????????????");
        }
        // ????????????????????????????????????
        if (product.getSaleAble() == 1) {
            return FCResponse.validateErrorResponse("?????????????????????????????????");
        }
        product.setSaleAble(-1);
        try {
            this.productMapper.updateProductStatus(product);
            // ???????????????????????????????????????
            log.info("????????????EL??????????????????????????????");
            this.amqpTemplate.convertAndSend("el.product.delete", id);
            log.info("????????????EL??????????????????????????????");
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_DELETE_SUCCESS.getMsg());
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ???????????? id??????????????????
     *
     * @param id
     * @return
     */
    @Override
    public FCResponse<ProductResponse> getProductResponseById(Long id) {
        ProductResponse productResponse = this.productMapper.getProductResponseById(id);
        if (ObjectUtils.isEmpty(productResponse)) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), "????????????");
        }
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), "??????????????????", productResponse);
    }

    /**
     * ????????????
     *
     * @param examineProductRequest@return
     */
    @Override
    public FCResponse<Void> examineProduct(ExamineProductRequest examineProductRequest) {
        try {
            Product product = new Product();
            product.setId(examineProductRequest.getProductId());
            product.setApproveStatus(examineProductRequest.getApproveStatus());
            product.setApproveRemark(examineProductRequest.getApproveRemark());
            this.productMapper.examineProduct(product);
            // ??????RabbitMQ?????????ElasticSearch??? TODO
            // ?????????????????????????????????EL???
            if (examineProductRequest.getApproveStatus() == 1) {
                // ????????????el???item
                ElSearchItem elSearchItem = new ElSearchItem();
                // ??????sku?????????????????????
                List<Map<String, Object>> skuMapList = new ArrayList<>();
                ProductResponse productResponse = this.productMapper.getProductResponseById(examineProductRequest.getProductId());
                elSearchItem.setProductId(productResponse.getId());
                elSearchItem.setCategoryId(productResponse.getCategoryId());
                elSearchItem.setCreateTime(productResponse.getCreateTime());
                elSearchItem.setUpdateTime(productResponse.getUpdateTime());
                elSearchItem.setView(productResponse.getView());
                elSearchItem.setStoreId(productResponse.getStoreId());
                elSearchItem.setSaleAble(productResponse.getSaleAble());
                // ????????????Id??????????????????????????????
                FCResponse<MerchantResponse> merchantFcResponse = this.merchantClient.getMerchantNumberAndNameById(productResponse.getMerchantId());
                if (!merchantFcResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
                    log.error("?????????{}????????????", productResponse.getId());
                    return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), "??????????????????");
                }
                MerchantResponse merchantResponse = merchantFcResponse.getData();
                // ??????????????????????????????
                elSearchItem.setMerchantName(merchantResponse.getMerchantName());
                elSearchItem.setMerchantNumber(merchantResponse.getNumber());
                // ????????????id??????????????????????????????
                List<Long> attributeIds = this.attributeMapper.getAttributeIdsByCategoryId(productResponse.getCategoryId());
                if (CollectionUtils.isEmpty(attributeIds)) {
                    return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.attribute.ATTRIBUTE_DATA_EMPTY.getMsg());
                }
                List<ParamResponse> paramResponses = this.paramMapper.getParamByAttributeId(attributeIds);
                if (CollectionUtils.isEmpty(paramResponses)) {
                    return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), "????????????????????????");
                }
                // ????????????id???????????????sku
                List<ProductSku> productSkus = this.productSkuMapper.getProductSkuByProductId(productResponse.getId());
                if (CollectionUtils.isEmpty(productSkus)) {
                    return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), "????????????Sku");
                }
                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder priceStringBuilder = new StringBuilder();
                StringBuilder titleStringBuilder = new StringBuilder();
                productSkus.forEach(productSku -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", productSku.getId());
                    map.put("title", productSku.getTitle());
                    map.put("price", productSku.getPrice());
                    map.put("images", productSku.getImages());
                    map.put("stock", productSku.getStock());
                    stringBuilder.append(productSku.getImages()).append(",");
                    priceStringBuilder.append(productSku.getPrice()).append(",");
                    titleStringBuilder.append(productSku.getTitle()).append(",");
                    skuMapList.add(map);
                });
                String image = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
                String prices = priceStringBuilder.deleteCharAt(priceStringBuilder.length() - 1).toString();
                String allTitle = titleStringBuilder.deleteCharAt(titleStringBuilder.length() - 1).toString();
                elSearchItem.setImages(image);
                elSearchItem.setPrices(prices);
                elSearchItem.setAllTitle(allTitle);
                // ????????????????????????????????????
                ProductDetail productDetail = this.productDetailMapper.getProductDetailByProductId(productResponse.getId());
                if (ObjectUtils.isEmpty(productDetail)) {
                    return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), "????????????????????????");
                }
                Map<String, Object> genericParamMap = JsonUtils.parseMap(productDetail.getGenericParam(), String.class, Object.class);
                Map<String, Object> specs = new HashMap<String, Object>();
                paramResponses.forEach(param -> {
                    assert genericParamMap != null;
                    String value = (String) genericParamMap.get(param.getId().toString());
                    specs.put(param.getName(), value);
                });
                elSearchItem.setSku(JsonUtils.serialize(skuMapList));
                elSearchItem.setSpecs(specs);
                String elSearchItemJson = JsonUtils.serialize(elSearchItem);
                this.amqpTemplate.convertAndSend("el.product.import", elSearchItemJson);
            }
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), "????????????");
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param id
     * @return
     */
    @Override
    public FCResponse<ProductResponse> getProductInfoById(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), "??????ID??????");
        }
        ProductResponse productResponse = this.productMapper.getProductResponseById(id);
        if (ObjectUtils.isEmpty(productResponse)) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), "????????????");
        }
        // ??????????????????
        ProductDetail productDetail = this.productDetailMapper.getProductDetailByProductId(id);
        Map<String, Object> genericParamMap = new HashMap<>();
        // ??????????????????????????????????????????
        Map<Long, Object> map = JsonUtils.parseMap(productDetail.getGenericParam(), Long.class, Object.class);
        assert map != null;
        Set<Long> longs = map.keySet();
        // ???????????????????????????
        for (Long paramId : longs) {
            Param param = this.paramMapper.getParamNameById(paramId);
            if (ObjectUtils.isNotEmpty(param)) {
                genericParamMap.put(param.getName(), map.get(paramId));
            }
        }
        productDetail.setGenericParamMap(genericParamMap);
        productResponse.setProductDetail(productDetail);
        // ????????????Sku
        List<ProductSku> productSkus = this.productSkuMapper.getProductSkuByProductId(id);
        // ???sku????????????????????????????????????????????????
        productSkus = productSkus.stream().peek(productSku -> {
            StringBuilder ownSpec = new StringBuilder();
            Map<Long, String> parseMap = JsonUtils.parseMap(productSku.getOwnSpec(), Long.class, String.class);
            // ??????map?????????
            assert parseMap != null;
            Set<Long> keys = parseMap.keySet();
            for (Long key : keys) {
                // ?????????
                String value = parseMap.get(key);
                ownSpec.append(value);
                ownSpec.append(" ");
            }
            productSku.setOwnSpec(ownSpec.toString());
            // ??????????????????
            Integer stock = this.stockMapper.getStockByProductSkuId(productSku.getId());
            productSku.setStock(stock);
        }).collect(Collectors.toList());
        productResponse.setProductSkus(productSkus);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), "????????????", productResponse);
    }
}
