package com.codeworld.fc.system.area.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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

/**
 * ClassName Area
 * Description 区域Model
 * Author Lenovo
 * Date 2021/1/29
 * Version 1.0
 **/
@TableName("codeworld_area")
@Table(name = "codeworld_area", comment = "区域model")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("区域model")
public class Area extends Model<Area> implements Serializable {

    @ApiModelProperty("区域Id")
    @TableField("t_area_id")
    @Column(name = "t_area_id", type = MySqlTypeConstant.BIGINT, length = 10, isNull = false, comment = "区域id")
    private Long id;

    @ApiModelProperty("名称")
    @TableField("t_area_name")
    @Column(name = "t_area_name", type = MySqlTypeConstant.VARCHAR, length = 255, isNull = false, comment = "名称")
    private String name;

    @ApiModelProperty("父级Id")
    @TableField("t_parent_id")
    @Column(name = "t_parent_id",type = MySqlTypeConstant.BIGINT,length = 10,isNull = false,comment = "父级id")
    private Long parentId;

    @ApiModelProperty("排序")
    @TableField("t_area_sort_no")
    @Column(name = "t_area_sort_no",type = MySqlTypeConstant.INT,length = 10,isNull = false,comment = "排序",defaultValue = "1")
    private Integer sortNo;
}
