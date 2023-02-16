package com.codeworld.fc.system.dept.entity;

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
 * ClassName Dept
 * Description 部门Model
 * Author Lenovo
 * Date 2020/11/26
 * Version 1.0
 **/
@TableName("codeworld_dept")
@Table(name = "codeworld_dept", comment = "部门model")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("部门model")
public class Dept extends Model<Dept> implements Serializable {

    @ApiModelProperty("部门主键Id")
    @TableField("t_dept_id")
    @Column(name = "t_dept_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "部门主键Id")
    private Long id;

    @ApiModelProperty("上级部门Id")
    @TableField("t_parent_id")
    @Column(name = "t_parent_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "部门上级Id")
    private Long parentId;

    @ApiModelProperty("部门名称")
    @TableField("t_dept_name")
    @Column(name = "t_dept_name", type = MySqlTypeConstant.VARCHAR, length = 255, isNull = false, comment = "部门名称")
    private String title;

    @ApiModelProperty("类型")
    @TableField("t_dept_type")
    @Column(name = "t_dept_type", type = MySqlTypeConstant.VARCHAR, length = 255, isNull = true, comment = "类型")
    private String type;

    @ApiModelProperty("部门简介")
    @TableField("t_dept_remarks")
    @Column(name = "t_dept_remarks", type = MySqlTypeConstant.TEXT, comment = "部门简介")
    private String remarks;

    @ApiModelProperty("排序")
    @TableField("t_dept_sort_no")
    @Column(name = "t_dept_sort_no", type = MySqlTypeConstant.INT, length = 10, isNull = false, comment = "排序")
    private Integer sortNo;

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
