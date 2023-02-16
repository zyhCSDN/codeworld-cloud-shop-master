package com.codeworld.fc.collection.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.codeworld.fc.collection.service.CollectionService;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;

/**
 * ClassName Collection
 * Description TODO
 * Author Lenovo
 * Date 2021/3/8
 * Version 1.0
**/
@TableName("codeworld_collection")
@Table(name = "codeworld_collection", comment = "商品收藏Model")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("商品收藏Model")
public class Collection extends Model<Collection> implements Serializable {

    @ApiModelProperty("收藏主键Id")
    @TableField("t_collection_id")
    @Column(name = "t_collection_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "收藏主键Id")
    private Long id;

    @ApiModelProperty("商品Id")
    @TableField("t_product_id")
    @Column(name = "t_product_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "商品Id")
    private Long productId;

    @ApiModelProperty("商品SkuId")
    @TableField("t_product_sku_id")
    @Column(name = "t_product_sku_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "商品SkuId")
    private Long productSkuId;

    @ApiModelProperty("会员Id")
    @TableField("t_member_id")
    @Column(name = "t_member_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "会员Id")
    private Long memberId;

    @ApiModelProperty("收藏时间")
    @TableField("t_collection_time")
    @Column(name = "t_collection_time", type = MySqlTypeConstant.DATETIME, comment = "收藏时间")
    private Date collectionTime;

    @ApiModelProperty("商品图片")
    @TableField("t_product_image")
    @Column(name = "t_product_image", type = MySqlTypeConstant.VARCHAR, length = 1000, comment = "商品图片")
    private String image;

    @ApiModelProperty("商品单价")
    @TableField("t_product_price")
    @Column(name = "t_product_price", type = MySqlTypeConstant.INT, length = 10, comment = "商品单价")
    private Integer price;

    @ApiModelProperty("商品标题")
    @TableField("t_product_title")
    @Column(name = "t_product_title", type = MySqlTypeConstant.VARCHAR, length = 225, comment = "商品标题")
    private String title;

}
