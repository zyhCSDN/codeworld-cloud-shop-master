package com.codeworld.fc.goods.stock.controller;

import com.codeworld.fc.common.response.FCResponse;
import com.codeworld.fc.goods.stock.domain.CommodityStocks;
import com.codeworld.fc.goods.stock.service.StockService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName StockController
 * Description 商品库存管理接口
 * Author Lenovo
 * Date 2021/1/13
 * Version 1.0
**/
@RestController
@RequestMapping("codeworld-goods/stock")
public class StockController {

    @Autowired(required = false)
    private StockService stockService;

    @PostMapping("update-product-stock-minus")
    @ApiOperation("更新库存（减掉）")
    public FCResponse<Void> updateProductStockMinus(@RequestBody CommodityStocks commodityStocks){
        return this.stockService.updateProductStockMinus(commodityStocks);
    }

    @PostMapping("judge-stock-adequate")
    @ApiOperation("判断库存是否充足")
    public FCResponse<Boolean> judgeStockAdequate(@RequestBody CommodityStocks commodityStocks){
        return this.stockService.judgeStockAdequate(commodityStocks);
    }

    @GetMapping("/get-stock-by-product-sku-id")
    @ApiOperation("根据商品skuid获取库存信息")
    public FCResponse<Integer> getStockByProductSkuId(@RequestParam("productSkuId") @ApiParam(value = "商品skuid",required = true) Long productSkuId){
        return this.stockService.getStockByProductSkuId(productSkuId);
    }
}
