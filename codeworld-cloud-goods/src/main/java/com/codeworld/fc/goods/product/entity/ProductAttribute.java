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

/**
 * ClassName ProductAttribute
 * Author Lenovo
**/
@TableName("codeworld_product_attribute")
@Table(name = "codeworld_product_attribute", comment = "商品和属性之间的关系")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel("商品和属性之间的关系")
public class ProductAttribute {

    @ApiModelProperty("商品id")
    @TableField("t_product_id")
    @Column(name = "t_product_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "商品Id")
    private Long productId;

    @ApiModelProperty("属性id")
    @TableField("t_attribute_id")
    @Column(name = "t_attribute_id", type = MySqlTypeConstant.BIGINT, length = 20, isNull = false, comment = "属性id")
    private Long attributeId;
}
