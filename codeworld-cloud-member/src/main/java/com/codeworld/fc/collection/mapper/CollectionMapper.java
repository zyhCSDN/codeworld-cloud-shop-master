package com.codeworld.fc.collection.mapper;

import com.codeworld.fc.collection.entity.Collection;
import com.codeworld.fc.collection.response.CollectionResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CollectionMapper {
    /**
     * 收藏商品
     * @param collection
     */
    void collectionProduct(Collection collection);

    /**
     * 获取会员收藏列表
     * @param memberId
     * @return
     */
    List<CollectionResponse> getMemberCollectionByPage(Long memberId);

    /**
     * 是否收藏商品
     * @param memberId
     * @param productId
     * @return
     */
    int isCollect(@Param("memberId") Long memberId, @Param("productId") Long productId);

    /**
     * 取消收藏
     * @param memberId
     * @param productId
     */
    void cancelCollect(@Param("memberId") Long memberId, @Param("productId") Long productId);

    /**
     * 获取商品收藏量
     * @param productId
     * @return
     */
    Long getCollectCountByProductId(Long productId);
}
