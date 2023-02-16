package com.codeworld.fc.member.entity;

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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName ReceiverAddress
 * Description 收货地址
 * Author Lenovo
 * Date 2020/12/23
 * Version 1.0
 **/
@TableName("codeworld_member_receiver_address")
@Table(name = "codeworld_member_receiver_address", comment = "收货地址")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("收货地址")
public class ReceiverAddress extends Model<ReceiverAddress> implements Serializable {

    @ApiModelProperty("地址主键Id")
    @TableField("t_address_id")
    @Column(name = "t_address_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "地址主键Id")
    private Long id;

    @ApiModelProperty("会员Id")
    @TableField("t_member_id")
    @Column(name = "t_member_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "会员Id")
    private Long memberId;

    @ApiModelProperty("收货人")
    @TableField("t_receiver_name")
    @Column(name = "t_receiver_name", type = MySqlTypeConstant.VARCHAR, length = 5, isNull = false, comment = "收货人")
    private String name;

    @ApiModelProperty("手机号码")
    @TableField("t_receiver_phone")
    @Column(name = "t_receiver_phone", type = MySqlTypeConstant.VARCHAR, length = 11, isNull = false, comment = "手机号码")
    private String phone;

    @ApiModelProperty("收货区域")
    @TableField("t_address_area")
    @Column(name = "t_address_area", type = MySqlTypeConstant.VARCHAR, length = 225, isNull = false, comment = "收货区域")
    private String area;

    @ApiModelProperty("详细地址")
    @TableField("t_address_detailed")
    @Column(name = "t_address_detailed", type = MySqlTypeConstant.VARCHAR, length = 225, isNull = false, comment = "详细地址")
    private String detailed;

    @ApiModelProperty("门牌号")
    @TableField("t_receiver_house_number")
    @Column(name = "t_receiver_house_number", type = MySqlTypeConstant.VARCHAR, length = 225, comment = "门牌号")
    private String houseNumber;

    @ApiModelProperty("状态 1--默认，0--其他")
    @TableField("t_address_status")
    @Column(name = "t_address_status", type = MySqlTypeConstant.INT, length = 1, isNull = false, comment = "收货地址状态 1--默认 0--其他", defaultValue = "0")
    private Integer status;

    @ApiModelProperty("创建时间")
    @TableField("t_address_create_time")
    @Column(name = "t_address_create_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField("t_address_update_time")
    @Column(name = "t_address_update_time", type = MySqlTypeConstant.DATETIME, comment = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
