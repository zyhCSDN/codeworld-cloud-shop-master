package com.codeworld.fc.goods.category.entity;

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
 * ClassName Category
 * Description 分类Model
 * Author Lenovo
 * Date 2020/11/27
 * Version 1.0
**/
@TableName("codeworld_category")
@Table(name = "codeworld_category", comment = "分类Model")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel("分类Model")
public class Category extends Model<Category> implements Serializable {

    @ApiModelProperty("分类主键Id")
    @TableField("t_category_id")
    @Column(name = "t_category_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "分类主键Id")
    private Long id;

    @ApiModelProperty("分类名称")
    @TableField("t_category_name")
    @Column(name = "t_category_name", type = MySqlTypeConstant.VARCHAR, length = 225, isNull = false, comment = "分类名称")
    private String name;

    @ApiModelProperty("父级Id")
    @TableField("t_category_parent_id")
    @Column(name = "t_category_parent_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "父级Id")
    private Long parentId;

    @ApiModelProperty("排序")
    @TableField("t_category_sort_no")
    @Column(name = "t_category_sort_no", type = MySqlTypeConstant.INT, length = 20, comment = "排序")
    private Integer sortNo;

    @ApiModelProperty("分类类型")
    @TableField("t_category_type")
    @Column(name = "t_category_type", type = MySqlTypeConstant.VARCHAR, length = 10, comment = "分类类型")
    private String type;

    @ApiModelProperty("分类图片")
    @TableField("t_category_image")
    @Column(name = "t_category_image", type = MySqlTypeConstant.VARCHAR, length = 1000, comment = "分类图片")
    private String image;

    @ApiModelProperty("创建时间")
    @TableField("t_category_create_time")
    @Column(name = "t_category_create_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField("t_category_update_time")
    @Column(name = "t_category_update_time", type = MySqlTypeConstant.DATETIME, comment = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
