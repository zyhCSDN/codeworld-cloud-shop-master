package com.codeworld.fc.order.entity;

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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName OrderDetail
 * Description 订单详细Model
 * Author Lenovo
 * Date 2020/12/28
 * Version 1.0
 **/
@TableName("codeworld_order_detail")
@Table(name = "codeworld_order_detail", comment = "订单详细model")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("订单详细model")
public class OrderDetail extends Model<OrderDetail> implements Serializable {

    @ApiModelProperty("订单详细Id")
    @TableField("t_order_detail_id")
    @Column(name = "t_order_detail_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "订单详情主键Id")
    private Long detailId;

    @ApiModelProperty("订单Id")
    @TableField("t_order_id")
    @Column(name = "t_order_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "订单id")
    private Long orderId;

    @ApiModelProperty("商品SkuId")
    @TableField("t_product_sku_id")
    @Column(name = "t_product_sku_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "商品skuid")
    private Long productSkuId;

    @ApiModelProperty("商品数量")
    @TableField("t_product_count")
    @Column(name = "t_product_count", type = MySqlTypeConstant.INT, length = 10, isNull = false, comment = "商品购买数量")
    private Integer productCount;

    @ApiModelProperty("商品标题")
    @TableField("t_product_title")
    @Column(name = "t_product_title", type = MySqlTypeConstant.VARCHAR, length = 255, isNull = true, comment = "商品标题")
    private String productTitle;

    @ApiModelProperty("商品Sku详情")
    @TableField("t_product_sku_detail")
    @Column(name = "t_product_sku_detail", type = MySqlTypeConstant.VARCHAR, length = 1000, isNull = false, comment = "商品Sku信息")
    private String productSkuDetail;

    @ApiModelProperty("商品价格")
    @TableField("t_product_price")
    @Column(name = "t_product_price", type = MySqlTypeConstant.BIGINT, length = 10, isNull = false, comment = "商品价格")
    private Long productPrice;

    @ApiModelProperty("商品图片")
    @TableField("t_product_image")
    @Column(name = "t_product_image",type = MySqlTypeConstant.VARCHAR,length = 1000,isNull = true,comment = "商品图片")
    private String productImage;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("t_create_time")
    @Column(name = "t_create_time", type = MySqlTypeConstant.DATETIME, isNull = true, comment = "创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("t_update_time")
    @Column(name = "t_update_time", type = MySqlTypeConstant.DATETIME, isNull = true, comment = "更新时间")
    private Date updateTime;

    @ApiModelProperty("商户号")
    @TableField("t_merchant_number")
    @Column(name = "t_merchant_number",type = MySqlTypeConstant.VARCHAR,length = 20,isNull = false,comment = "商户号")
    private String merchantNumber;

    @ApiModelProperty("订单物流公司")
    @TableField("t_order_delivery_company")
    @Column(name = "t_order_delivery_company",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = true,comment = "订单物流公司")
    private String orderDeliveryCompany;

    @ApiModelProperty("订单物流编号")
    @TableField("t_order_delivery_sn")
    @Column(name = "t_order_delivery_sn",type = MySqlTypeConstant.INT,length = 10,isNull = true,comment = "物流编号")
    private Integer orderDeliverySn;

    @ApiModelProperty("订单物流单号")
    @TableField("t_order_delivery_number")
    @Column(name = "t_order_delivery_number",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = true,comment = "物流单号")
    private String orderDeliveryNumber;

}
