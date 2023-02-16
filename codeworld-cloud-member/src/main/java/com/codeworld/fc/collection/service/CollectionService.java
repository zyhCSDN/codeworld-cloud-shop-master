package com.codeworld.fc.collection.service;

import com.codeworld.fc.collection.request.CollectionRequest;
import com.codeworld.fc.collection.response.CollectionResponse;
import com.codeworld.fc.common.response.FCResponse;

import java.util.List;

public interface CollectionService {
    /**
     * 收藏商品
     * @param collectionRequest
     * @return
     */
    FCResponse<Void> collectionProduct(CollectionRequest collectionRequest);

    /**
     * 获取会员商品收藏列表
     * @return
     */
    FCResponse<List<CollectionResponse>> getMemberCollectionByPage();

    /**
     * 是否收藏商品
     * @param productId
     * @return
     */
    FCResponse<Boolean> isCollect(Long productId);

    /**
     * 取消收藏
     * @param productId
     * @return
     */
    FCResponse<Void> cancelCollect(Long productId);

    /**
     * 获取商品收藏量
     * @param productId
     * @return
     */
    FCResponse<Long> getCollectCountByProductId(Long productId);
}
