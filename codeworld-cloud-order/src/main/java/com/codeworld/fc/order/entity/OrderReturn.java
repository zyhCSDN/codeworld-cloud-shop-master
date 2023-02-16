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
 * ClassName OrderReturn
 * Description 订单退款退货
 * Author Lenovo
 * Date 2021/1/25
 * Version 1.0
**/
@TableName("codeworld_order_return")
@Table(name = "codeworld_order_return", comment = "订单退款退货")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("订单退款退货")
public class OrderReturn {

    @ApiModelProperty("退货订单主键id")
    @TableField("t_return_order_id")
    @Column(name = "t_return_order_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "退货订单主键id")
    private Long orderReturnId;

    @ApiModelProperty("原订单id")
    @TableField("t_order_id")
    @Column(name = "t_order_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "订单id 对饮订单明细中的t_order_detail_id")
    private Long orderId;

    @ApiModelProperty("订单退货退款状态 订单退货 退款状态 0--待处理 1--退款中 2--退款成功 3--退货中 4--退货成功 5--拒绝退款 6---拒绝退货")
    @TableField("t_return_order_status")
    @Column(name = "t_return_order_status",type = MySqlTypeConstant.INT,length = 1,isNull = false,comment = "订单退货 退款状态 0--待处理 1--退款中 2--退款成功 3--退货中 4--退货成功 5--拒绝退款 6---拒绝退货")
    private Integer orderReturnStatus;

    @ApiModelProperty("订单退款退货类型 1--退款 3--退货")
    @TableField("t_return_order_type")
    @Column(name = "t_return_order_type",type = MySqlTypeConstant.INT,length = 1,isNull = false,comment = "订单服务类型 1--退款 3---退货")
    private Integer orderReturnType;

    @ApiModelProperty("订单退货退款处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("t_return_handle_time")
    @Column(name = "t_return_handle_time",type = MySqlTypeConstant.DATETIME,isNull = true,comment = "处理时间")
    private Date orderReturnHandleTime;

    @ApiModelProperty("退货原因")
    @TableField("t_return_order_reason")
    @Column(name = "t_return_order_reason",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = true,comment = "退货原因")
    private String orderReturnReason;

    @ApiModelProperty("订单退款退货申请时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("t_return_order_apply_time")
    @Column(name = "t_return_order_apply_time",type = MySqlTypeConstant.DATETIME,isNull = false,comment = "订单退款退货申请时间")
    private Date orderReturnApplyTime;

    @ApiModelProperty("订单退款退货备注")
    @TableField("t_return_order_remark")
    @Column(name = "t_return_order_remark",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = true,comment = "备注信息")
    private String orderReturnRemark;
}
