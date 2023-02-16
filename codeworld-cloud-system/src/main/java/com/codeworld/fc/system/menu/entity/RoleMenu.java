package com.codeworld.fc.system.menu.entity;

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
 * ClassName RoleMenu
 * Description 角色菜单类
 * Author Lenovo
 * Date 2020/9/14
 * Version 1.0
**/
@TableName("codeworld_role_menu")
@Table(name = "codeworld_role_menu", comment = "角色菜单类")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("角色菜单类")
public class RoleMenu extends Model<RoleMenu> implements Serializable {

    @ApiModelProperty("角色菜单主键Id")
    @TableField("t_role_menu_id")
    @Column(name = "t_role_menu_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "角色菜单主键id")
    private Long roleMenuId;

    @ApiModelProperty("角色Id")
    @TableField("t_role_id")
    @Column(name = "t_role_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = true,comment = "角色id")
    private Long roleId;

    @ApiModelProperty("菜单Id")
    @TableField("t_menu_id")
    @Column(name = "t_menu_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "菜单id")
    private Long menuId;

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

