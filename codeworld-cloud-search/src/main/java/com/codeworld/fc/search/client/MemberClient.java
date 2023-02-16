package com.codeworld.fc.search.client;

import com.codeworld.fc.common.response.FCResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("codeworld-cloud-member")
public interface MemberClient {

    @GetMapping("/codeworld-collection/get-collect-count-by-product-id")
    @ApiOperation("获取商品收藏量")
    FCResponse<Long> getCollectCountByProductId(@RequestParam(value = "productId", required = true) @ApiParam(value = "商品id", required = true) Long productId);
}
