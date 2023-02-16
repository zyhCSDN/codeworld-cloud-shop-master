package com.codeworld.fc.merchant.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gitee.sunchenbin.mybatis.actable.annotation.Column;
import com.gitee.sunchenbin.mybatis.actable.annotation.Table;
import com.gitee.sunchenbin.mybatis.actable.constants.MySqlTypeConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName MerchantWxInfo
 * Description 商户微信信息
 * Author Lenovo
 * Date 2021/3/19
 * Version 1.0
**/
@TableName("codeworld_merchant_wxinfo")
@Table(name = "codeworld_merchant_wxinfo", comment = "商户微信信息")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("商户微信信息")
public class MerchantWxInfo {

    @ApiModelProperty("商户号")
    @TableField("t_merchant_number")
    @Column(name = "t_merchant_number",type = MySqlTypeConstant.VARCHAR,length = 20,isNull = false,comment = "商户号")
    private String merchantNumber;

    @ApiModelProperty("商户OpenId")
    @TableField("t_open_id")
    @Column(name = "t_open_id",type = MySqlTypeConstant.VARCHAR,length = 20,isNull = false,comment = "商户OpedId")
    private String openId;
}

