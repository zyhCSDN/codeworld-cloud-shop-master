package com.codeworld.fc.goods.attribute.entity;

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
 * ClassName CategoryAttribute
 * Description 分类属性Model
 * Author Lenovo
 * Date 2020/12/3
 * Version 1.0
**/
@TableName("codeworld_category_attribute")
@Table(name = "codeworld_category_attribute", comment = "分类属性Model")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("分类属性Model")
public class CategoryAttribute extends Model<CategoryAttribute> implements Serializable {

    @ApiModelProperty("分类属性id")
    @TableField("t_category_attribute_id")
    @Column(name = "t_category_attribute_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "分类属性id")
    private Long id;

    @ApiModelProperty("分类id")
    @TableField("t_category_id")
    @Column(name = "t_category_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "分类id")
    private Long categoryId;

    @ApiModelProperty("属性id")
    @TableField("t_attribute_id")
    @Column(name = "t_attribute_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "属性id")
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
