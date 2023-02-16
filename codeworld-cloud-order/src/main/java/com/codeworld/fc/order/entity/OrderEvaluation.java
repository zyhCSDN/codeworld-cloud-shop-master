package com.codeworld.fc.order.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * ClassName OrderEvaluation
 * Description TODO
 * Author Lenovo
 * Date 2021/2/20
 * Version 1.0
 **/
@TableName("codeworld_order_evaluation")
@Table(name = "codeworld_order_evaluation", comment = "订单评价")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("订单评价")
public class OrderEvaluation {

    @ApiModelProperty("订单详情id")
    @TableField("t_order_detail_id")
    @Column(name = "t_order_detail_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "订单详情id")
    private Long orderDetailId;

    @ApiModelProperty("订单商品评价内容")
    @TableField("t_order_evaluation_content")
    @Column(name = "t_order_evaluation_content", type = MySqlTypeConstant.TEXT, isNull = false, comment = "订单评价内容")
    private String evaluationContent;

    @ApiModelProperty("订单商品评价图片")
    @TableField("t_order_evaluation_image")
    @Column(name = "t_order_evaluation_image", type = MySqlTypeConstant.VARCHAR, length = 255, isNull = false, comment = "订单评价图片")
    private String evaluationImage;

    @ApiModelProperty("评分")
    @TableField("t_order_evaluation_rate")
    @Column(name = "t_order_evaluation_rate", type = MySqlTypeConstant.INT, length = 1, isNull = false, comment = "订单商品评分")
    private Integer evaluationRate;

    @ApiModelProperty("评价时间")
    @TableField("t_order_evaluation_time")
    @Column(name = "t_order_evaluation_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "评价时间")
    private Date evaluationTime;
}
