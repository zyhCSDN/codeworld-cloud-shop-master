package com.codeworld.fc.system.role.entity;

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
 * ClassName Role
 * Description 角色Model
 * Author Lenovo
 * Date 2020/11/26
 * Version 1.0
**/

@TableName("codeworld_role")
@Table(name = "codeworld_role", comment = "角色model")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("角色model")
public class Role extends Model<Role> implements Serializable {

    @ApiModelProperty("角色主键Id")
    @TableField("t_role_id")
    @Column(name = "t_role_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "角色主键id")
    private Long roleId;

    @ApiModelProperty("角色编码")
    @TableField("t_role_code")
    @Column(name = "t_role_code",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "角色编码")
    private String roleCode;

    @ApiModelProperty("角色名称")
    @TableField("t_role_name")
    @Column(name = "t_role_name",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "角色名称")
    private String roleName;

    @ApiModelProperty("角色描述")
    @TableField("t_role_remarks")
    @Column(name = "t_role_remarks",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "角色描述")
    private String roleRemarks;

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
