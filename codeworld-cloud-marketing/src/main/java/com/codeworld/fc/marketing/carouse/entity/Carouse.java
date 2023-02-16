package com.codeworld.fc.marketing.carouse.entity;

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
 * ClassName IndexCarouse
 * Description 首页轮播图
 * Author Lenovo
 * Date 2021/1/13
 * Version 1.0
**/
@TableName("codeworld_logistics")
@Table(name = "codeworld_logistics", comment = "物流信息Model")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("首页轮播图")
public class Carouse extends Model<Carouse> implements Serializable {

    @ApiModelProperty("轮播图主键Id")
    @TableField("t_carouse_id")
    @Column(name = "t_carouse_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "轮播图主键Id")
    private Long id;

    @ApiModelProperty("轮播图标题")
    @TableField("t_carouse_title")
    @Column(name = "t_carouse_title", type = MySqlTypeConstant.VARCHAR, length = 225, comment = "轮播图标题")
    private String title;

    @ApiModelProperty("轮播图图片")
    @TableField("t_carouse_image")
    @Column(name = "t_carouse_image", type = MySqlTypeConstant.VARCHAR, length = 1000, comment = "轮播图图片")
    private String image;

    @ApiModelProperty("轮播图状态 0--未启用 1--已启用")
    @TableField("t_carouse_status")
    @Column(name = "t_carouse_status", type = MySqlTypeConstant.INT, length = 1, isNull = false, comment = "轮播图状态 0--未启用 1--已启用", defaultValue = "1")
    private Integer status;

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("t_carouse_start_time")
    @Column(name = "t_carouse_start_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("t_carouse_end_time")
    @Column(name = "t_carouse_end_time", type = MySqlTypeConstant.DATETIME, comment = "结束时间")
    private Date endTime;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("t_carouse_create_time")
    @Column(name = "t_carouse_create_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "创建时间")
    private Date createTime;

    @ApiModelProperty("审核状态 0--未审核 1--审核通过 -1--审核未通过")
    @TableField("t_carouse_review_status")
    @Column(name = "t_carouse_review_status", type = MySqlTypeConstant.INT, length = 1, comment = "审核状态 0--未审核 1--审核通过 -1--审核未通过")
    private Integer reviewStatus;

    @ApiModelProperty("轮播图位置 1--app首页 2--店铺")
    @TableField("t_carouse_position")
    @Column(name = "t_carouse_position", type = MySqlTypeConstant.INT, length = 1, isNull = false, comment = "轮播图位置 1--app首页 2--店铺")
    private Integer position;

    @ApiModelProperty("商户号")
    @TableField("t_merchant_number")
    @Column(name = "t_merchant_number", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "商户号")
    private String merchantNumber;

}
