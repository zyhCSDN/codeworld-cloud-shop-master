package com.codeworld.fc.search.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.codeworld.fc.common.enums.HttpFcStatus;
import com.codeworld.fc.common.enums.HttpMsg;
import com.codeworld.fc.common.exception.FCException;
import com.codeworld.fc.common.response.DataResponse;
import com.codeworld.fc.common.response.FCResponse;
import com.codeworld.fc.common.utils.JsonUtils;
import com.codeworld.fc.common.utils.StringUtil;
import com.codeworld.fc.search.client.*;
import com.codeworld.fc.search.domain.*;
import com.codeworld.fc.search.item.SearchItem;
import com.codeworld.fc.search.repository.SearchRepository;
import com.codeworld.fc.search.request.ProductIndexSearchRequest;
import com.codeworld.fc.search.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ClassName GoodsServiceImpl
 * Description TODO
 * Author Lenovo
 * Date 2020/12/15
 * Version 1.0
 **/
@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {


    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private ParamClient paramClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private MerchantClient merchantClient;
    @Autowired
    private StockClient stockClient;
    @Autowired
    private MemberClient memberClient;

    @Autowired
    private SearchRepository searchRepository;
    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;
    // ???????????????????????????redis????????????
    private static final String PRODUCT_VIEW = "PRODUCT_VIEW:";


    /**
     * ???????????????ElasticSearch???
     *
     * @return
     */
    public FCResponse<Void> importGoodsToElasticSearch() {

        // ??????????????????
        Integer page = 1;
        // ?????????????????????
        int limit = 100;
        // ??????????????????
        // ??????????????????
        ProductSearchRequest productSearchRequest = new ProductSearchRequest();
        productSearchRequest.setPage(page);
        productSearchRequest.setLimit(limit);
        productSearchRequest.setSaleAble(1);
        Long startTime = System.currentTimeMillis();
        log.info("??????????????????");
        do {
            FCResponse<DataResponse<List<ProductResponse>>> response = this.goodsClient.getPageProduct(productSearchRequest);
            if (!response.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
                return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.product.PRODUCT_DATA_EMPTY.getMsg());
            }
            if (CollectionUtils.isEmpty(response.getData().getData())) {
                return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.product.PRODUCT_DATA_EMPTY.getMsg());
            }
            // ??????????????????
            List<ProductResponse> productResponses = response.getData().getData();
            if (CollectionUtils.isEmpty(productResponses)) {
                return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.product.PRODUCT_DATA_EMPTY.getMsg());
            }
            // ??????????????????
            List<SearchItem> searchItems = productResponses.stream().map(productResponse -> {
                try {
                    return this.buildSearchItems(productResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new FCException("????????????");
                }
            }).collect(Collectors.toList());
            this.searchRepository.saveAll(searchItems);
            limit = productResponses.size();
            page++;
            productSearchRequest.setPage(page);
        } while (limit == 100);
        Long endTime = System.currentTimeMillis();
        log.info("???????????????????????????{}", endTime - startTime);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_IMPORT_SUCCESS.getMsg());
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @Override
    public FCResponse<Void> deleteAllGoodsToElasticSearch() {
        this.searchRepository.deleteAll();
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_DELETE_ELASTICSEARCH.getMsg());
    }

    /**
     * ????????????????????????
     *
     * @param productIndexSearchRequest
     * @return
     */
    @Override
    public FCResponse<List<ProductResponse>> getNewProductIndex(ProductIndexSearchRequest productIndexSearchRequest) {
        try {
            // ????????????????????????
            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            // ??????????????????,?????????????????????
            queryBuilder.withQuery(QueryBuilders.termQuery("saleAble", 1));
            // ????????????
            queryBuilder.withPageable(PageRequest.of(productIndexSearchRequest.getPageFrom(), productIndexSearchRequest.getPageSize()));
            Page<SearchItem> searchResponse = this.searchRepository.search(queryBuilder.build());
            if (CollectionUtils.isEmpty(searchResponse.getContent())) {
                return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.product.PRODUCT_DATA_EMPTY.getMsg(), null);
            }
            List<SearchItem> searchItems = searchResponse.getContent();
            List<ProductResponse> productResponses = searchItems.stream().map(searchItem -> {
                return this.buildProductResponse(searchItem);
            }).collect(Collectors.toList());
            return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_GET_SUCCESS.getMsg(), productResponses);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FCException("????????????");
        }
    }

    /**
     * ????????????id????????????
     *
     * @param productId
     * @return
     */
    @Override
    public FCResponse<ProductResponse> getProductById(Long productId) {
        if (ObjectUtil.isEmpty(productId) || productId <= 0) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.product.PRODUCT_ID_ERROR.getMsg());
        }
        // ????????????????????????
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // ??????????????????
        queryBuilder.withQuery(QueryBuilders.termQuery("productId", productId));
        // ???????????????
        Page<SearchItem> search = this.searchRepository.search(queryBuilder.build());
        if (CollectionUtils.isEmpty(search.getContent())) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.product.PRODUCT_DATA_EMPTY.getMsg());
        }
        SearchItem searchItem = search.getContent().get(0);
        ProductResponse productResponse = this.buildProductResponse(searchItem);
        // ????????????????????????????????????
        this.stringRedisTemplate.opsForValue().increment(PRODUCT_VIEW + productId.toString(), 1L);
        log.info("??????????????????????????????Id???{}", productId);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_GET_SUCCESS.getMsg(), productResponse);
    }

    /**
     * ?????????????????????????????????
     *
     * @return
     */
    @Override
    public FCResponse<Void> importGoodsToElasticSearchTime() {
        // ??????????????????
        Integer page = 1;
        // ?????????????????????
        Integer limit = 100;
        // ??????????????????
        // ??????????????????
        ProductSearchRequest productSearchRequest = new ProductSearchRequest();
        productSearchRequest.setPage(page);
        productSearchRequest.setLimit(limit);
        productSearchRequest.setSaleAble(1);
        Long startTime = System.currentTimeMillis();
        log.info("????????????????????????,??????????????????{}", DateUtil.date(new Date()));
        do {
            FCResponse<DataResponse<List<ProductResponse>>> response = this.goodsClient.getPageProductTime(productSearchRequest);
            if (!response.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
                return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), response.getMsg());
            }
            if (CollectionUtils.isEmpty(response.getData().getData())) {
                return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.product.PRODUCT_DATA_EMPTY.getMsg());
            }
            // ??????????????????
            List<ProductResponse> productResponses = response.getData().getData();
            if (CollectionUtils.isEmpty(productResponses)) {
                return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.product.PRODUCT_DATA_EMPTY.getMsg());
            }
            // ??????????????????
            List<SearchItem> searchItems = productResponses.stream().map(productResponse -> {
                try {
                    return this.buildSearchItems(productResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new FCException("????????????");
                }
            }).collect(Collectors.toList());
            this.searchRepository.saveAll(searchItems);
            limit = productResponses.size();
            page++;
            productSearchRequest.setPage(page);
        } while (limit == 100);
        Long endTime = System.currentTimeMillis();
        log.info("?????????????????????????????????{}", endTime - startTime);
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_IMPORT_SUCCESS.getMsg());
    }

    /**
     * ????????????id????????????
     *
     * @param productSearchRequest
     * @return
     */
    @Override
    public FCResponse<List<ProductResponse>> getProductByCategoryId(ProductSearchRequest productSearchRequest) {

        if (StringUtils.isEmpty(productSearchRequest.getSubCateId())) {
            return FCResponse.dataResponse(HttpFcStatus.PARAMSERROR.getCode(), HttpMsg.category.CATEGORY_ID_ERROR.getMsg());
        }
        // ????????????????????????
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // ??????????????????
        queryBuilder.withQuery(QueryBuilders.termQuery("categoryId", productSearchRequest.getSubCateId()));
        // ????????????
        queryBuilder.withPageable(PageRequest.of(productSearchRequest.getPage(), productSearchRequest.getLimit()));
        Page<SearchItem> searchResponse = this.searchRepository.search(queryBuilder.build());
        if (CollectionUtils.isEmpty(searchResponse.getContent())) {
            return FCResponse.dataResponse(HttpFcStatus.DATAEMPTY.getCode(), HttpMsg.product.PRODUCT_DATA_EMPTY.getMsg(), null);
        }
        List<SearchItem> searchItems = searchResponse.getContent();
        List<ProductResponse> productResponses = searchItems.stream().map(searchItem -> {
            return this.buildProductResponse(searchItem);
        }).collect(Collectors.toList());
        return FCResponse.dataResponse(HttpFcStatus.DATASUCCESSGET.getCode(), HttpMsg.product.PRODUCT_GET_SUCCESS.getMsg(), productResponses);
    }

    /**
     * ??????????????????
     *
     * @param elProductStatusDTO
     */
    @Override
    public Boolean updateProductStatus(ElProductStatusDTO elProductStatusDTO) {

        // ???????????????Id??????????????????
        // ????????????????????????
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // ??????????????????
        queryBuilder.withQuery(QueryBuilders.termQuery("productId", elProductStatusDTO.getProductId()));
        // ???????????????
        Page<SearchItem> search = this.searchRepository.search(queryBuilder.build());
        if (CollectionUtils.isEmpty(search.getContent())) {
            log.error("????????????????????????,?????????{}", elProductStatusDTO);
            return false;
        }
        SearchItem searchItem = search.getContent().get(0);
        // ????????????
        searchItem.setSaleAble(elProductStatusDTO.getSaleAble());
        try {
            this.searchRepository.save(searchItem);
            log.info("ElasticSearch???????????????????????????");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("ElasticSearch???????????????????????????");
            return false;
        }
    }

    /**
     * ????????????
     *
     * @param id
     * @return
     */
    @Override
    public Boolean deleteGoods(Long id) {
        try {
            this.searchRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ??????????????????????????????ElasticSearch???
     *
     * @param
     * @return
     */
    @Override
    public Boolean importGoodsSoon(String elSearchItemJson) {
        log.info("??????????????????ElasticSearch??????");
        try {
            SearchItem searchItem = JsonUtils.parse(elSearchItemJson, SearchItem.class);
            if (ObjectUtils.isEmpty(searchItem)) {
                log.error("??????????????????ElasticSearch??????????????????????????????????????????");
                return false;
            }
            this.searchRepository.save(searchItem);
            log.info("??????????????????ElasticSearch??????");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("??????????????????ElasticSearch??????");
            throw new FCException("????????????");
        }
    }

    /**
     * ?????????????????????????????????productResponse
     *
     * @param searchItem
     * @return
     */
    private ProductResponse buildProductResponse(SearchItem searchItem) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(searchItem.getProductId());
        productResponse.setCategoryId(searchItem.getCategoryId());
        // ????????????id??????????????????
        FCResponse<Category> categoryResponse = this.categoryClient.getCategoryById(searchItem.getCategoryId());
        if (ObjectUtil.isEmpty(categoryResponse.getData())) {
            return productResponse;
        }
        Category category = categoryResponse.getData();
        productResponse.setCategoryName(category.getName());
        productResponse.setCreateTime(searchItem.getCreateTime());
        productResponse.setUpdateTime(searchItem.getUpdateTime());
        // ????????????
        List<String> images = Arrays.asList(searchItem.getImages().split(","));
        productResponse.setImages(images);
        // ????????????
        List<String> prices = Arrays.asList(searchItem.getPrices().split(","));
        productResponse.setPrice(Integer.parseInt(prices.get(0)));
        // ????????????
        List<String> allTitle = Arrays.asList(searchItem.getAllTitle().split(","));
        productResponse.setTitle(allTitle.get(0));
        // ??????????????????
        List<String> specialParam = StringUtil.getFirstBlankString(searchItem.getAllTitle());
        productResponse.setSpecialParam(specialParam);
        productResponse.setSku(searchItem.getSku());
        productResponse.setMerchantName(searchItem.getMerchantName());
        productResponse.setMerchantNumber(searchItem.getMerchantNumber());
        productResponse.setView(searchItem.getView());
        productResponse.setStoreId(searchItem.getStoreId());
        productResponse.setCollectCount(searchItem.getCollectCount());
        return productResponse;
    }


    /**
     * ??????????????????
     *
     * @param productResponse
     * @return
     */
    private SearchItem buildSearchItems(ProductResponse productResponse) {

        // ??????sku?????????????????????
        List<Map<String, Object>> skuMapList = new ArrayList<>();
        SearchItem searchItem = new SearchItem();
        searchItem.setProductId(productResponse.getId());
        searchItem.setCategoryId(productResponse.getCategoryId());
//        searchItem.setTitle(productResponse.getTitle());
        searchItem.setCreateTime(productResponse.getCreateTime());
        searchItem.setUpdateTime(productResponse.getUpdateTime());
        searchItem.setView(productResponse.getView());
        searchItem.setStoreId(productResponse.getStoreId());
        searchItem.setSaleAble(productResponse.getSaleAble());
        // ????????????Id??????????????????????????????
        FCResponse<MerchantResponse> merchantFcResponse = this.merchantClient.getMerchantNumberAndNameById(productResponse.getMerchantId());
        if (!merchantFcResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
            log.error("?????????{}????????????", productResponse.getId());
            return null;
        }
        MerchantResponse merchantResponse = merchantFcResponse.getData();
        // ??????????????????????????????
        searchItem.setMerchantName(merchantResponse.getMerchantName());
        searchItem.setMerchantNumber(merchantResponse.getNumber());
        // ???????????????????????????
        FCResponse<Long> collectResponse = this.memberClient.getCollectCountByProductId(productResponse.getId());
        searchItem.setCollectCount(collectResponse.getData());
        // ????????????id??????????????????????????????
        FCResponse<List<ParamResponse>> paramResponse = this.paramClient.getParamByCategoryId(productResponse.getCategoryId());
        if (!paramResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
            log.error("?????????{}???????????????", productResponse.getId());
            return null;
        }
        if (CollectionUtils.isEmpty(paramResponse.getData())) {
            return null;
        }
        List<ParamResponse> paramResponses = paramResponse.getData();

        // ????????????id???????????????sku
        FCResponse<List<ProductSku>> productSkuResponse = this.goodsClient.getProductSkuByProductId(productResponse.getId());
        if (!productSkuResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
            log.error("?????????{}???Sku", productResponse.getId());
            return null;
        }
        if (CollectionUtils.isEmpty(productSkuResponse.getData())) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder priceStringBuilder = new StringBuilder();
        StringBuilder titleStringBuilder = new StringBuilder();
        List<ProductSku> productSkus = productSkuResponse.getData();
        productSkus.forEach(productSku -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", productSku.getId());
            map.put("title", productSku.getTitle());
            map.put("price", productSku.getPrice());
            map.put("images", productSku.getImages());
            // ????????????sku??????
            FCResponse<Integer> response = this.stockClient.getStockByProductSkuId(productSku.getId());
            Integer stock = response.getData();
            map.put("stock", stock);
            stringBuilder.append(productSku.getImages()).append(",");
            priceStringBuilder.append(productSku.getPrice()).append(",");
            titleStringBuilder.append(productSku.getTitle()).append(",");
            skuMapList.add(map);
        });
        String image = stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
        String prices = priceStringBuilder.deleteCharAt(priceStringBuilder.length() - 1).toString();
        String allTitle = titleStringBuilder.deleteCharAt(titleStringBuilder.length() - 1).toString();
        searchItem.setImages(image);
        searchItem.setPrices(prices);
        searchItem.setAllTitle(allTitle);
        // ????????????????????????????????????
        FCResponse<ProductDetail> productDetailResponse = this.goodsClient.getProductDetailByProductId(productResponse.getId());
        if (!productDetailResponse.getCode().equals(HttpFcStatus.DATASUCCESSGET.getCode())) {
            log.error("??????{}???????????????", productResponse.getId());
            return null;
        }
        if (ObjectUtil.isEmpty(productDetailResponse.getData())) {
            return null;
        }
        ProductDetail productDetail = productDetailResponse.getData();
        Map<String, Object> genericParamMap = JsonUtils.parseMap(productDetail.getGenericParam(), String.class, Object.class);
        Map<String, Object> specs = new HashMap<String, Object>();
        paramResponses.forEach(param -> {
            assert genericParamMap != null;
            String value = (String) genericParamMap.get(param.getId().toString());
            specs.put(param.getName(), value);
        });
        searchItem.setSku(JsonUtils.serialize(skuMapList));
        searchItem.setSpecs(specs);
        return searchItem;
    }
}
