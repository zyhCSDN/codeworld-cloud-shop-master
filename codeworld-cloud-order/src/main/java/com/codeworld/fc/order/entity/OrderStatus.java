package com.codeworld.fc.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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

import java.util.Date;

/**
 * ClassName OrderStatus
 * Description 订单状态Model
 * Author Lenovo
 * Date 2020/12/28
 * Version 1.0
**/
@TableName("codeworld_order_status")
@Table(name = "codeworld_order_status", comment = "订单状态")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("订单状态Model")
public class OrderStatus {

    @ApiModelProperty("订单Id")
    @TableField("t_order_id")
    @Column(name = "t_order_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "订单id 对饮订单明细中的t_order_detail_id")
    private Long orderId;

    /**
     * 订单状态 1、未付款 2、已付款,未发货 3、已发货,未确认 4、交易成功 5、交易关闭 6、已评价 7、失效订单
     * 8、售后服务 9、未评价
     */
    @ApiModelProperty("订单状态")
    @TableField("t_order_status")
    @Column(name = "t_order_status",type = MySqlTypeConstant.INT,length = 1,isNull = false,comment = "订单状态 1、未付款 2、已付款,未发货 3、已发货,未确认 4、交易成功 5、交易关闭 6、已评价 7、失效订单\n" +
            "8、售后服务 9、未评价")
    private Integer orderStatus;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("t_order_create_time")
    @Column(name = "t_order_create_time",type = MySqlTypeConstant.DATETIME,isNull = false,comment = "订单创建时间")
    private Date createTime;

    @ApiModelProperty("支付时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("t_order_pay_time")
    @Column(name = "t_order_pay_time",type = MySqlTypeConstant.DATETIME,isNull = true,comment = "订单付款时间")
    private Date payTime;

    @ApiModelProperty("发货时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("t_order_consign_time")
    @Column(name = "t_order_consign_time",type = MySqlTypeConstant.DATETIME,isNull = true,comment = "订单发货时间")
    private Date consignTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("t_order_end_time")
    @Column(name = "t_order_end_time",type = MySqlTypeConstant.DATETIME,isNull = true,comment = "订单结束时间")
    private Date endTime;

    @ApiModelProperty("关闭时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("t_order_close_time")
    @Column(name = "t_order_close_time",type = MySqlTypeConstant.DATETIME,isNull = true,comment = "订单关闭时间")
    private Date closeTime;
}
