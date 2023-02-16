package com.codeworld.fc.cart.entity;

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
 * ClassName Cart
 * Description 购物车Model
 * Author Lenovo
 * Date 2020/12/25
 * Version 1.0
**/
@TableName("codeworld_cart")
@Table(name = "codeworld_cart", comment = "购物车Model")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("购物车Model")
public class Cart extends Model<Cart> implements Serializable {

    @ApiModelProperty("购物车主键Id")
    @TableField("t_cart_id")
    @Column(name = "t_cart_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "购物车主键Id")
    private Long id;

    @ApiModelProperty("会员id")
    @TableField("t_cart_member_id")
    @Column(name = "t_cart_member_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "会员id")
    private Long memberId;

    @ApiModelProperty("商品id")
    @TableField("t_cart_product_id")
    @Column(name = "t_cart_product_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "商品id")
    private Long productId;

    @ApiModelProperty("商品SkuId")
    @TableField("t_cart_product_sku_id")
    @Column(name = "t_cart_product_sku_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = true, comment = "商品Skuid")
    private Long productSkuId;

    @ApiModelProperty("商品标题")
    @TableField("t_cart_product_title")
    @Column(name = "t_cart_product_title", type = MySqlTypeConstant.VARCHAR, length = 225, isNull = false, comment = "商品标题")
    private String productTitle;

    @ApiModelProperty("商品数量")
    @TableField("t_cart_product_price")
    @Column(name = "t_cart_product_price", type = MySqlTypeConstant.INT, length = 10, isNull = false, comment = "商品数量")
    private Integer productCount;

    @ApiModelProperty("商品价格")
    @TableField("t_cart_product_count")
    @Column(name = "t_cart_product_count", type = MySqlTypeConstant.INT, length = 10, isNull = false, comment = "商品价格")
    private Integer productPrice;

    @ApiModelProperty("商品是否删除 0--未删除 1--已删除")
    @TableField("t_cart_product_status")
    @Column(name = "t_cart_product_status", type = MySqlTypeConstant.INT, length = 1, isNull = false, comment = "商品是否删除 0--未删除 1--已删除", defaultValue = "0")
    private Integer productStatus;

    @ApiModelProperty("商品图片")
    @TableField("t_cart_product_image")
    @Column(name = "t_cart_product_image", type = MySqlTypeConstant.VARCHAR, length = 1000, comment = "商品图片")
    private String productImage;

    @ApiModelProperty("商品Sku")
    @TableField("t_cart_product_sku")
    @Column(name = "t_cart_product_sku", type = MySqlTypeConstant.VARCHAR, length = 1000, comment = "商品Sku")
    private String productSku;

    @ApiModelProperty("创建时间")
    @TableField("t_cart_create_time")
    @Column(name = "t_cart_create_time", type = MySqlTypeConstant.DATETIME, comment = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:ss:mm",timezone = "GTM+8")
    private Date createTime;

    @ApiModelProperty("商户编号")
    @TableField("t_cart_merchant_number")
    @Column(name = "t_cart_merchant_number", type = MySqlTypeConstant.VARCHAR, length = 20, comment = "商户编号")
    private String merchantNumber;
}
