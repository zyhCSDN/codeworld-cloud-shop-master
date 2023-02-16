package com.codeworld.fc.member.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.codeworld.fc.collection.entity.Collection;
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
 * ClassName Member
 * Description 会员Model
 * Author Lenovo
 * Date 2020/12/18
 * Version 1.0
**/
@TableName("codeworld_member")
@Table(name = "codeworld_member", comment = "会员Model")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("会员Model")
public class Member extends Model<Member> implements Serializable {

    @ApiModelProperty("会员主键Id")
    @TableField("t_member_id")
    @Column(name = "t_member_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "会员主键Id")
    private Long id;

    @ApiModelProperty("会员名称")
    @TableField("t_member_name")
    @Column(name = "t_member_name", type = MySqlTypeConstant.VARCHAR, length = 10, isNull = false, comment = "会员名称")
    private String name;

    @ApiModelProperty("会员密码")
    @TableField("t_member_password")
    @Column(name = "t_member_password", type = MySqlTypeConstant.VARCHAR, length = 20, isNull = false, comment = "会员密码")
    private String password;

    @ApiModelProperty("会员手机号")
    @TableField("t_member_phone")
    @Column(name = "t_member_phone", type = MySqlTypeConstant.VARCHAR, length = 11, isNull = false, comment = "会员手机号")
    private String phone;

    @ApiModelProperty("会员创建时间")
    @TableField("t_member_create_time")
    @Column(name = "t_member_create_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "会员创建时间")
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("会员更新时间")
    @TableField("t_member_update_time")
    @Column(name = "t_member_update_time", type = MySqlTypeConstant.BIGINT, comment = "会员更新时间")
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
}
