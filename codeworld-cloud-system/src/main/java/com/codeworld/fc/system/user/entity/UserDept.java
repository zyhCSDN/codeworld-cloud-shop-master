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
 * ClassName UserDept
 * Description 用户部门
 * Author Lenovo
 * Date 2020/10/14
 * Version 1.0
**/
@TableName("codeworld_user_dept")
@Table(name = "codeworld_user_dept", comment = "用户部门")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("用户部门")
public class UserDept extends Model<UserDept> implements Serializable {

    @ApiModelProperty("用户部门主键Id")
    @TableField("t_user_dept_id")
    @Column(name = "t_user_dept_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "用户部门主键Id")
    private Long userDeptId;

    @ApiModelProperty("用户Id")
    @TableField("t_user_id")
    @Column(name = "t_user_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "用户id")
    private Long userId;

    @ApiModelProperty("部门Id")
    @TableField("t_dept_id")
    @Column(name = "t_dept_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "部门id")
    private Long deptId;

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
