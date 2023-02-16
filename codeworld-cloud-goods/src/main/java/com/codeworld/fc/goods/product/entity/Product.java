package com.codeworld.fc.goods.product.entity;

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
 * ClassName Product
 * Description 商品Model
 * Author Lenovo
 * Date 2020/11/28
 * Version 1.0
**/
@TableName("codeworld_product")
@Table(name = "codeworld_product", comment = "商品Model")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("商品Model")
public class Product extends Model<Product> implements Serializable {

    @ApiModelProperty("商品Id")
    @TableField("t_product_id")
    @Column(name = "t_product_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "商品Id")
    private Long id;

    @ApiModelProperty("商品标题")
    @TableField("t_product_title")
    @Column(name = "t_product_title", type = MySqlTypeConstant.VARCHAR, length = 225, isNull = false, comment = "商品标题")
    private String title;

    @ApiModelProperty("商品二级标题")
    @TableField("t_product_sub_title")
    @Column(name = "t_product_sub_title", type = MySqlTypeConstant.VARCHAR, length = 225, comment = "商品二级标题")
    private String subTitle;

    @ApiModelProperty("分类Id")
    @TableField("t_product_category_id")
    @Column(name = "t_product_category_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "商品分类Id")
    private Long categoryId;

    @ApiModelProperty("品牌Id")
    @TableField("t_product_brand_id")
    @Column(name = "t_product_brand_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "商品品牌Id")
    private Long brandId;

    @ApiModelProperty("商品状态 1-上架 0-下架")
    @TableField("t_product_saleable")
    @Column(name = "t_product_saleable", type = MySqlTypeConstant.INT, length = 1, isNull = false, comment = "商品状态 1-上架 0-下架", defaultValue = "1")
    private Integer saleAble;

    @ApiModelProperty("商户Id")
    @TableField("t_product_merchant_id")
    @Column(name = "t_product_merchant_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "商户Id")
    private Long merchantId;

    @ApiModelProperty("添加时间")
    @TableField("t_product_create_time")
    @Column(name = "t_product_create_time", type = MySqlTypeConstant.DATETIME, isNull = false, comment = "添加时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField("t_product_update_time")
    @Column(name = "t_product_update_time", type = MySqlTypeConstant.BIGINT, comment = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("审核状态 1--审核通过 0--审核未通过 -1--未审核")
    @TableField("t_product_approve_status")
    @Column(name = "t_product_approve_status", type = MySqlTypeConstant.INT, length = 1, comment = "审核状态 1--审核通过 0--审核未通过 -1--未审核")
    private Integer approveStatus;

    @ApiModelProperty("审核备注信息")
    @TableField("t_product_approve_remark")
    @Column(name = "t_product_approve_remark", type = MySqlTypeConstant.VARCHAR, length = 225, comment = "审核备注信息")
    private String approveRemark;

    @ApiModelProperty("商品浏览量")
    @TableField("t_product_view_count")
    @Column(name = "t_product_view_count", type = MySqlTypeConstant.BIGINT, length = 20, comment = "商品浏览量")
    private Long viewCount;
}
