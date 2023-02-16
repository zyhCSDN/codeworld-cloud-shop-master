package com.codeworld.fc.logistics.entity;

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
 * ClassName Logistics
 * Description TODO
 * Author Lenovo
 * Date 2021/3/3
 * Version 1.0
**/
@TableName("codeworld_logistics")
@Table(name = "codeworld_logistics", comment = "物流信息Model")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("物流信息Model")
public class Logistics extends Model<Logistics> implements Serializable {

    @ApiModelProperty("物流主键Id")
    @TableField("t_logistics_id")
    @Column(name = "t_logistics_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "物流主键Id")
    private Long id;

    @ApiModelProperty("物流单号")
    @TableField("t_logistics_delivery_number")
    @Column(name = "t_logistics_delivery_number", type = MySqlTypeConstant.VARCHAR, length = 225, isNull = false, comment = "物流单号")
    private String deliveryNumber;

    @ApiModelProperty("物流编号")
    @TableField("t_logistics_delivery_sn")
    @Column(name = "t_logistics_delivery_sn", type = MySqlTypeConstant.INT, length = 11, isNull = false, comment = "物流编号")
    private Integer deliverySn;

    @ApiModelProperty("订单号")
    @TableField("t_logistics_order_id")
    @Column(name = "t_logistics_order_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "订单号")
    private Long orderId;

    @ApiModelProperty("物流到达时间")
    @TableField("t_logistics_arrive_time")
    @Column(name = "t_logistics_arrive_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "物流到达时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date arriveTime;

    @ApiModelProperty("物流显示内容")
    @TableField("t_logistics_content")
    @Column(name = "t_logistics_content", type = MySqlTypeConstant.VARCHAR, length = 225, isNull = false, comment = "物流显示内容")
    private String content;

    @ApiModelProperty("操作员Id")
    @TableField("t_logistics_operation_id")
    @Column(name = "t_logistics_operation_id", type = MySqlTypeConstant.BIGINT, length = 20, comment = "操作员Id")
    private Long operationId;

    @ApiModelProperty("操作员姓名")
    @TableField("t_logistics_operation_name")
    @Column(name = "t_logistics_operation_name", type = MySqlTypeConstant.VARCHAR, length = 225, comment = "操作员姓名")
    private String operationName;
}
