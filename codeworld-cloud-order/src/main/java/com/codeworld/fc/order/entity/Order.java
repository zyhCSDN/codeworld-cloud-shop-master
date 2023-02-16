package com.codeworld.fc.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName Order
 * Description 订单Model
 * Author Lenovo
 * Date 2020/12/28
 * Version 1.0
**/
@TableName("codeworld_order")
@Table(name = "codeworld_order", comment = "订单model")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("订单model")
public class Order extends Model<Order> implements Serializable {

    @ApiModelProperty("订单Id")
    @TableField("t_order_id")
    @Column(name = "t_order_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "订单id")
    private Long Id;

    @ApiModelProperty("订单总金额")
    @TableField("t_order_total_pay")
    @Column(name = "t_order_total_pay",type = MySqlTypeConstant.DECIMAL,decimalLength = 2,isNull = false,comment = "订单总金额")
    private Long totalPay;

    @ApiModelProperty("订单实付金额")
    @TableField("t_order_actual_pay")
    @Column(name = "t_order_actual_pay",type = MySqlTypeConstant.DECIMAL,decimalLength = 2,isNull = true,comment = "订单实付金额")
    private Long actualPay;

    @ApiModelProperty("订单支付类型")
    @TableField("t_order_pay_type")
    @Column(name = "t_order_pay_type",type = MySqlTypeConstant.INT,length = 1,isNull = true,comment = "支付类型 1--在线支付 2--货到付款")
    private Integer payType;

    @ApiModelProperty("邮费")
    @TableField("t_order_post_fee")
    @Column(name = "t_order_post_fee",type = MySqlTypeConstant.BIGINT,length = 20,isNull = true,comment = "邮费")
    private Long postFee;

    @ApiModelProperty("会员Id")
    @TableField("t_order_member_id")
    @Column(name = "t_order_member_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "会员id")
    private Long memberId;

    @ApiModelProperty("地址Id")
    @TableField("t_order_address_id")
    @Column(name = "t_order_address_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "地址Id")
    private Long addressId;

    @ApiModelProperty("买家备注")
    @TableField("t_order_buyer_remarks")
    @Column(name = "t_order_buyer_remarks",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = true,comment = "买家备注信息")
    private String buyerRemarks;

    @ApiModelProperty("买家会员名")
    @TableField("t_order_buyer_name")
    @Column(name = "t_order_buyer_name",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "买家会员名")
    private String buyerName;

    @ApiModelProperty("商户号")
    @TableField("t_order_merchant_number")
    @Column(name = "t_order_merchant_number",type = MySqlTypeConstant.VARCHAR,length = 20,isNull = false,comment = "商户号")
    private String merchantNumber;

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

    @ApiModelProperty("订单下总共的商品数量")
    @TableField("t_order_product_count")
    @Column(name = "t_order_product_count",type = MySqlTypeConstant.INT,length = 10,isNull = true,comment = "订单下总共的商品数量")
    private Integer orderProductCount;

}
