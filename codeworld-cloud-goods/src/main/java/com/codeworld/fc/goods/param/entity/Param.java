package com.codeworld.fc.goods.param.entity;

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
 * ClassName Param
 * Description 属性参数Model
 * Author Lenovo
 * Date 2020/12/7
 * Version 1.0
**/
@TableName("codeworld_param")
@Table(name = "codeworld_param", comment = "属性参数Model")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("属性参数Model")
public class Param extends Model<Param> implements Serializable {

    @ApiModelProperty("参数主键Id")
    @TableField("t_param_id")
    @Column(name = "t_param_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "参数主键Id")
    private Long id;

    @ApiModelProperty("属性名")
    @TableField("t_param_name")
    @Column(name = "t_param_name", type = MySqlTypeConstant.VARCHAR, length = 225, isNull = false, comment = "属性名")
    private String name;

    @ApiModelProperty("特殊属性 1--是，0--不是")
    @TableField("t_special_param")
    @Column(name = "t_special_param", type = MySqlTypeConstant.INT, length = 1, isNull = false, comment = "特殊属性 1--是，0--不是", defaultValue = "0")
    private Integer specialParam;

    @ApiModelProperty("属性Id")
    @TableField("t_attribute_id")
    @Column(name = "t_attribute_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "属性Id")
    private Long attributeId;

    @ApiModelProperty("创建时间")
    @TableField("t_create_time")
    @Column(name = "t_create_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField("t_update_time")
    @Column(name = "t_update_time", type = MySqlTypeConstant.DATETIME, comment = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
}
