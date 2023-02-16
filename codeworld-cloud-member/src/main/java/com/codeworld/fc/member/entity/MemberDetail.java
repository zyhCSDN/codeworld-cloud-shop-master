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

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName MemberDetail
 * Description 会员详细信息Model
 * Author Lenovo
 * Date 2020/12/25
 * Version 1.0
**/
@TableName("codeworld_member_detail")
@Table(name = "codeworld_member_detail", comment = "会员详细信息Model")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("会员详细信息Model")
public class MemberDetail extends Model<MemberDetail> implements Serializable {

    @ApiModelProperty("会员Id")
    @TableField("t_member_id")
    @Column(name = "t_member_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "会员主键Id")
    private Long memberId;

    @ApiModelProperty("头像")
    @TableField("t_member_avatar")
    @Column(name = "t_member_avatar", type = MySqlTypeConstant.VARCHAR, length = 1000, comment = "头像")
    private String avatar;

    @ApiModelProperty("性别")
    @TableField("t_member_sex")
    @Column(name = "t_member_sex", type = MySqlTypeConstant.INT, length = 1, comment = "性别")
    private Integer sex;

    @ApiModelProperty("创建时间")
    @TableField("t_member_create_time")
    @Column(name = "t_member_create_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField("t_member_update_time")
    @Column(name = "t_member_update_time", type = MySqlTypeConstant.DATETIME, comment = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
}
