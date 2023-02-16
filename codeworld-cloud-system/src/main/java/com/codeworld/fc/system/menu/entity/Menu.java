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
 * ClassName Menu
 * Description 菜单Model
 * Author Lenovo
 * Date 2020/11/26
 * Version 1.0
**/
@TableName("codeworld_menu")
@Table(name = "codeworld_menu", comment = "菜单model")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("菜单model")
public class Menu extends Model<Menu> implements Serializable {

    @ApiModelProperty("菜单Id")
    @TableField("t_menu_id")
    @Column(name = "t_menu_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "菜单Id")
    private Long id;

    @ApiModelProperty("上级Id")
    @TableField("t_parent_id")
    @Column(name = "t_parent_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "菜单上级Id")
    private Long parentId;

    @ApiModelProperty("菜单Url")
    @TableField("t_menu_url")
    @Column(name = "t_menu_url",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "菜单url")
    private String url;

    @ApiModelProperty("菜单名称")
    @TableField("t_menu_name")
    @Column(name = "t_menu_name",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "菜单名称")
    private String title;

    @ApiModelProperty("菜单编码")
    @TableField(value = "t_menu_resources")
    @Column(name = "t_menu_resources",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "菜单编码")
    private String resources;

    @ApiModelProperty("类型")
    @TableField("t_menu_type")
    @Column(name = "t_menu_type",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "类型")
    private String type;

    @ApiModelProperty("菜单简介")
    @TableField("t_menu_remarks")
    @Column(name = "t_menu_remarks",type = MySqlTypeConstant.TEXT,comment = "菜单简介")
    private String remarks;

    @ApiModelProperty("排序")
    @TableField("t_menu_sort_no")
    @Column(name = "t_menu_sort_no",type = MySqlTypeConstant.INT,length = 10,isNull = false,comment = "排序")
    private Integer sortNo;

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
}
