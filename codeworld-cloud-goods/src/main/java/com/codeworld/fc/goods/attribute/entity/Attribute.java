package com.codeworld.fc.goods.attribute.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.codeworld.fc.goods.param.entity.Param;
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
import java.util.List;

/**
 * ClassName Attribute
 * Description 属性Model
 * Author Lenovo
 * Date 2020/12/4
 * Version 1.0
**/
@TableName("codeworld_attribute")
@Table(name = "codeworld_attribute", comment = "属性Model")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("属性Model")
public class Attribute extends Model<Attribute> implements Serializable {

    @ApiModelProperty("属性主键Id")
    @TableField("t_attribute_id")
    @Column(name = "t_attribute_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "属性主键Id")
    private Long id;

    @ApiModelProperty("属性名")
    @TableField("t_attribute_name")
    @Column(name = "t_attribute_name", type = MySqlTypeConstant.VARCHAR, length = 225, isNull = false, comment = "属性名")
    private String name;

    @ApiModelProperty("分类Id")
    @TableField("t_category_id")
    @Column(name = "t_category_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "分类Id")
    private Long categoryId;

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

    @ApiModelProperty("规格属性下的全部参数")
    @TableField("t_is_spec")
    @Column(name = "t_is_spec", type = MySqlTypeConstant.TINYINT, length = 1, isNull = false, comment = "是否是规格属性 1--是 0不是", defaultValue = "0")
    private List<Param> params;
}
