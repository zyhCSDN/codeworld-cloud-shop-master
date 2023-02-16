package com.codeworld.fc.store.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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

import java.util.Date;

/**
 * ClassName StoreCategory
 * Description TODO
 * Author Lenovo
 * Date 2021/3/10
 * Version 1.0
**/
@TableName("codeworld_store_category")
@Table(name = "codeworld_store_category", comment = "店铺分类")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("店铺分类")
public class StoreCategory {

    @ApiModelProperty("店铺分类Id")
    @TableField("t_store_category_id")
    @Column(name = "t_store_category_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "店铺分类Id")
    private Long id;

    @ApiModelProperty("店铺Id")
    @TableField("t_store_id")
    @Column(name = "t_store_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "店铺id")
    private Long storeId;

    @ApiModelProperty("分类父级Id")
    @TableField("t_parent_id")
    @Column(name = "t_parent_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "店铺分类父级Id")
    private Long parentId;

    @ApiModelProperty("分类名")
    @TableField("t_category_name")
    @Column(name = "t_category_name",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "分类名称")
    private String name;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("t_store_create_time")
    @Column(name = "t_store_create_time",type = MySqlTypeConstant.DATETIME,isNull = true,comment = "创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("t_store_update_time")
    @Column(name = "t_store_update_time",type = MySqlTypeConstant.DATETIME,isNull = true,comment = "更新时间")
    private Date updateTime;

    @ApiModelProperty("排序")
    @TableField("t_sort_no")
    @Column(name = "t_sort_no",type = MySqlTypeConstant.INT,length = 10,isNull = false,comment = "排序")
    private Integer sortNo;
}
