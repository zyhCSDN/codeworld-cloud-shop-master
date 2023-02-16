package com.codeworld.fc.goods.stock.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName Stock
 * Description 商品库存Model
 * Author Lenovo
 * Date 2021/1/13
 * Version 1.0
**/
@TableName("codeworld_stock")
@Table(name = "codeworld_stock", comment = "商品库存Model")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("商品库存Model")
public class Stock {

    @ApiModelProperty("商品SkuId")
    @TableField("t_product_sku_id")
    @Column(name = "t_product_sku_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "商品SkuId")
    private Long productSkuId;

    @ApiModelProperty("商品库存")
    @TableField("t_product_stock")
    @Column(name = "t_product_stock", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "商品库存")
    private Integer stock;
}
