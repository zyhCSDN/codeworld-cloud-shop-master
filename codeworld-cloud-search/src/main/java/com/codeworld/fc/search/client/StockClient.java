package com.codeworld.fc.search.client;

import com.codeworld.fc.common.response.FCResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "codeworld-cloud-goods")
public interface StockClient {

    @GetMapping("/codeworld-goods/stock/get-stock-by-product-sku-id")
    @ApiOperation("根据商品skuid获取库存信息")
    FCResponse<Integer> getStockByProductSkuId(@RequestParam("productSkuId") @ApiParam(value = "商品skuid", required = true) Long productSkuId);
}
