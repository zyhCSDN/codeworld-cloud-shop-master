package com.codeworld.fc.goods.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName ProductSku
 * Description 商品Sku
 * Author Lenovo
 * Date 2020/12/10
 * Version 1.0
 **/
@TableName("codeworld_product_sku")
@Table(name = "codeworld_product_sku", comment = "商品Sku")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("商品Sku")
public class ProductSku extends Model<ProductSku> implements Serializable {

    @ApiModelProperty("商品Sku主键Id")
    @TableField("t_product_sku_id")
    @Column(name = "t_product_sku_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "商品Sku主键Id")
    private Long id;

    @ApiModelProperty("商品Id")
    @TableField("t_product_id")
    @Column(name = "t_product_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "商品Id")
    private Long productId;

    @ApiModelProperty("商品Sku标题")
    @TableField("t_product_sku_title")
    @Column(name = "t_product_sku_title", type = MySqlTypeConstant.VARCHAR, length = 225, comment = "商品Sku标题")
    private String title;

    @ApiModelProperty("商品Sku价格")
    @TableField("t_product_sku_price")
    @Column(name = "t_product_sku_price", type = MySqlTypeConstant.INT, length = 10, isNull = false, comment = "商品Sku价格")
    private Integer price;

    @ApiModelProperty("商品Sku价格参数")
    @TableField("t_product_sku_own_spec")
    @Column(name = "t_product_sku_own_spec", type = MySqlTypeConstant.TEXT, comment = "商品Sku价格参数")
    private String ownSpec;

    @ApiModelProperty("商品图片")
    @TableField("t_product_sku_images")
    @Column(name = "t_product_sku_images", type = MySqlTypeConstant.TEXT, comment = "商品图片")
    private String images;

    @ApiModelProperty("创建时间")
    @TableField("t_product_sku_create_time")
    @Column(name = "t_product_sku_create_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField("t_product_sku_update_time")
    @Column(name = "t_product_sku_update_time", type = MySqlTypeConstant.DATETIME, comment = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("库存")
    @TableField("t_product_sku_stock")
    @Column(name = "t_product_sku_stock", type = MySqlTypeConstant.INT, length = 10, comment = "库存")
    private Integer stock;
}
