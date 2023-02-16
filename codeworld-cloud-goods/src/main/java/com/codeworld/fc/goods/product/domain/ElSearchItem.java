package com.codeworld.fc.goods.product.domain;

import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * ClassName ElSearchItem
 * Author Lenovo
 * 构建导出el的item
**/
@Data
public class ElSearchItem{

    // 商品Id
    private Long productId;

    // 商品标题
    private String title;

    // 分类Id
    private Long categoryId;

    // 商品图片
    private String images;

    // 商品价格
    private String prices;

    // 商品标题
    private String allTitle;

    // 商品状态 1--上架 0--下架
    private Integer saleAble;

    // 商户号
    private String merchantNumber;

    // 商家名称
    private String merchantName;

    // 创建时间
    private Date createTime;

    // 更细时间
    private Date updateTime;

    // 商品Sku
    private String sku;

    // 可搜索的规格参数，key是参数名，值是参数值
    private Map<String, Object> specs;

    // 商品特有参数
    private String specialParam;

    // 商品浏览量
    private Long view;

    // 门店Id
    private Long storeId;
}
