package com.codeworld.fc.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.codeworld.fc.system.role.entity.Role;
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
 * ClassName User
 * Description 用户Model
 * Author Lenovo
 * Date 2020/11/29
 * Version 1.0
**/
@TableName("codeworld_user")
@Table(name = "codeworld_user", comment = "用户model")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户model")
public class User extends Model<User> implements Serializable {

    @ApiModelProperty("主键Id")
    @TableField("t_user_id")
    @Column(name = "t_user_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "用户主键Id")
    private Long userId;

    @ApiModelProperty("用户名")
    @TableField("t_user_name")
    @Column(name = "t_user_name",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "用户名")
    private String userName;

    @ApiModelProperty("用户邮箱")
    @TableField("t_user_email")
    @Column(name = "t_user_email",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "用户邮箱")
    private String userEmail;

    @ApiModelProperty("用户手机")
    @TableField("t_user_phone")
    @Column(name = "t_user_phone",type = MySqlTypeConstant.VARCHAR,length = 11,isNull = false,comment = "手机")
    private String userPhone;

    @ApiModelProperty("用户状态 1--启用 0--禁用")
    @TableField("t_user_status")
    @Column(name = "t_user_status",type = MySqlTypeConstant.INT,length = 1,isNull = false,comment = "用户状态 1--启用 0--禁用")
    private Integer userStatus;

    @ApiModelProperty("用户区域")
    @TableField("t_area_name")
    @Column(name = "t_area_name",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "用户所属区域")
    private String areaName;

    @ApiModelProperty("区域Id")
    @TableField("t_area_id")
    @Column(name = "t_area_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "区域id")
    private Long areaId;

    @ApiModelProperty("用户头像")
    @TableField("t_user_avatar")
    @Column(name = "t_user_avatar",type = MySqlTypeConstant.VARCHAR,length = 1000,isNull = true,comment = "头像")
    private String avatar;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:ss:mm",timezone = "GTM+8")
    @TableField("t_create_time")
    @Column(name = "t_create_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:ss:mm",timezone = "GTM+8")
    @TableField("t_update_time")
    @Column(name = "t_update_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "更新时间")
    private Date updateTime;

    @ApiModelProperty("密码")
    @TableField("t_user_password")
    @Column(name = "t_user_password",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "用户密码")
    private String password;

    @ApiModelProperty("密码盐值")
    @TableField("t_user_password_salt")
    @Column(name = "t_user_password_salt",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "用户密码加盐加密")
    private String passwordSalt;

    @ApiModelProperty("用户备注")
    @TableField("t_user_remark")
    @Column(name = "t_user_remark",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = true,comment = "用户备注")
    private String remark;

    @ApiModelProperty("关联的角色信息")
    @TableField(exist = false)
    private Role role;

    @ApiModelProperty("是否删除 1--删除 0--未删除")
    @TableField("t_user_deleted")
    @Column(name = "t_user_deleted",type = MySqlTypeConstant.TINYINT,length = 1,isNull = false,comment = "用户是否删除 1--删除 0--未删除",defaultValue = "0")
    private Short deleted;
}
