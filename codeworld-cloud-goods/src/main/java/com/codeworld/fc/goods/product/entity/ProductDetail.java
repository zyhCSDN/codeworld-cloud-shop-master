package com.codeworld.fc.goods.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

/**
 * ClassName ProductDetail
 * Description 商品详情
 * Author Lenovo
 * Date 2020/12/2
 * Version 1.0
**/
@TableName("codeworld_product_detail")
@Table(name = "codeworld_product_detail", comment = "商品详情")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("商品详情")
public class ProductDetail {

    @ApiModelProperty("商品id")
    @TableField("t_product_id")
    @Column(name = "t_product_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "商品Id")
    private Long id;

    @ApiModelProperty("商品描述信息")
    @TableField("t_product_desc")
    @Column(name = "t_product_desc", type = MySqlTypeConstant.TEXT, comment = "商品描述信息")
    private String desc;

    @ApiModelProperty("商品通用参数")
    @TableField("t_product_generic_param")
    @Column(name = "t_product_generic_param", type = MySqlTypeConstant.TEXT, comment = "商品通用参数")
    private String genericParam;

    @ApiModelProperty("商品特殊参数")
    @TableField("t_product_special_param")
    @Column(name = "t_product_special_param", type = MySqlTypeConstant.TEXT, comment = "商品特殊参数")
    private String specialParam;

    @ApiModelProperty("商品包装信息")
    @TableField("t_product_packing_list")
    @Column(name = "t_product_packing_list", type = MySqlTypeConstant.TEXT, comment = "商品包装信息")
    private String packingList;

    @ApiModelProperty("商品售后服务")
    @TableField("t_product_after_service")
    @Column(name = "t_product_after_service", type = MySqlTypeConstant.TEXT, comment = "商品售后服务")
    private String afterService;

    @ApiModelProperty("创建时间")
    @TableField("t_product_create_time")
    @Column(name = "t_product_create_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField("t_product_update_time")
    @Column(name = "t_product_update_time", type = MySqlTypeConstant.DATETIME, comment = "更新时间")
    private Date updateTime;

    @ApiModelProperty("商品浏览量")
    @TableField("t_product_view_count")
    @Column(name = "t_product_view_count", type = MySqlTypeConstant.BIGINT, length = 20, comment = "商品浏览量")
    private Long view;

    @ApiModelProperty("通用参数map类型")
    @TableField(exist = false)
    private Map<String, Object> genericParamMap;
}
