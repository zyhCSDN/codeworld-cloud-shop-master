package com.codeworld.fc.system.user.entity;

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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName UserRole
 * Description 用户角色类
 * Author Lenovo
 * Date 2020/11/4
 * Version 1.0
**/
@TableName("codeworld_user_role")
@Table(name = "codeworld_user_role", comment = "用户角色")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户角色")
public class UserRole extends Model<UserRole> implements Serializable {

    @ApiModelProperty("用户角色主键Id")
    @TableField("t_user_role_id")
    @Column(name = "t_user_role_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "用户角色主键Id")
    private Long userRoleId;

    @ApiModelProperty("用户Id")
    @TableField("t_user_id")
    @Column(name = "t_user_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "用户id")
    private Long userId;

    @ApiModelProperty("角色Id")
    @TableField("t_role_id")
    @Column(name = "t_role_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "角色id")
    private Long roleId;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("t_create_time")
    @Column(name = "t_create_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("t_update_time")
    @Column(name = "t_update_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "更新时间")
    private Date updateTime;

}
