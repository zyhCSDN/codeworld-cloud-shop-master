package com.codeworld.fc.collection.controller;

import com.codeworld.fc.collection.request.CollectionRequest;
import com.codeworld.fc.collection.response.CollectionResponse;
import com.codeworld.fc.collection.service.CollectionService;
import com.codeworld.fc.common.response.FCResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * ClassName CollectionController
 * Description 商品收藏接口
 * Author Lenovo
 * Date 2021/3/8
 * Version 1.0
 **/
@RestController
@RequestMapping("codeworld-collection")
@Api(tags = "商品收藏接口")
public class CollectionController {

    @Autowired(required = false)
    private CollectionService collectionService;

    @PostMapping("/app/collection-product")
    @ApiOperation("收藏商品")
    public FCResponse<Void> collectionProduct(@RequestBody @Valid CollectionRequest collectionRequest) {
        return this.collectionService.collectionProduct(collectionRequest);
    }

    @GetMapping("/app/get-member-collection-page")
    @ApiOperation("获取会员商品收藏列表")
    public FCResponse<List<CollectionResponse>> getMemberCollectionByPage() {
        return this.collectionService.getMemberCollectionByPage();
    }

    @GetMapping("/app/is-collect/{productId}")
    @ApiOperation("是否收藏商品")
    public FCResponse<Boolean> isCollect(@PathVariable(value = "productId", required = true) @ApiParam(value = "商品id", required = true) Long productId) {
        return this.collectionService.isCollect(productId);
    }

    @PutMapping("/app/cancel-collect/{productId}")
    @ApiOperation("取消收藏")
    public FCResponse<Void> cancelCollect(@PathVariable(value = "productId", required = true) @ApiParam(value = "商品id", required = true) Long productId) {
        return this.collectionService.cancelCollect(productId);
    }

    @GetMapping("/get-collect-count-by-product-id")
    @ApiOperation("获取商品收藏量")
    public FCResponse<Long> getCollectCountByProductId(@RequestParam(value = "productId", required = true) @ApiParam(value = "商品id", required = true) Long productId) {
        return this.collectionService.getCollectCountByProductId(productId);
    }
}
