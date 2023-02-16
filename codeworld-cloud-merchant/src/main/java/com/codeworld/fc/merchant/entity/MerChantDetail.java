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

import java.util.Date;

/**
 * ClassName MerChantDetail
 * Description 商户详细信息Model
 * Author Lenovo
 * Date 2020/12/30
 * Version 1.0
**/
@TableName("codeworld_merchant_detail")
@Table(name = "codeworld_merchant_detail", comment = "商户详细信息Model")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("商户详细信息Model")
public class MerChantDetail {

    @ApiModelProperty("商户号")
    @TableField("t_merchant_number")
    @Column(name = "t_merchant_number",type = MySqlTypeConstant.VARCHAR,length = 20,isNull = false,comment = "商户号")
    private String merchantNumber;

    @ApiModelProperty("商家名称")
    @TableField("t_merchant_name")
    @Column(name = "t_merchant_name",type = MySqlTypeConstant.VARCHAR,length = 20,isNull = true,comment = "商家昵称")
    private String merchantName;

    @ApiModelProperty("身份证正面")
    @TableField("t_merchant_id_card_front")
    @Column(name = "t_merchant_id_card_front",type = MySqlTypeConstant.VARCHAR,length = 1000,isNull = true,comment = "身份证正面")
    private String idCardFront;

    @ApiModelProperty("身份证反面")
    @TableField("t_merchant_id_card_back")
    @Column(name = "t_merchant_id_card_back",type = MySqlTypeConstant.VARCHAR,length = 1000,isNull = true,comment = "身份证反面")
    private String idCardBack;

    @ApiModelProperty("营业执照")
    @TableField("t_merchant_license")
    @Column(name = "t_merchant_license",type = MySqlTypeConstant.VARCHAR,length = 1000,isNull = true,comment = "营业执照")
    private String license;

    @ApiModelProperty("入住类型 1--包月 2--包季 3--包年")
    @TableField("t_merchant_type")
    @Column(name = "t_merchant_type",type = MySqlTypeConstant.INT,length = 1,isNull = true,comment = "入驻类型 1--包月 2--包季 3--包年")
    private Integer type;

    @ApiModelProperty("审核状态 1--审核成功， 审核失败")
    @TableField("t_merchant_status")
    @Column(name = "t_merchant_status",type = MySqlTypeConstant.INT,length = 1,isNull = false,defaultValue = "2",comment = "状态 1--已审核，且审核，2--未审核 0--审核失败 3--未入驻")
    private Integer status;

    @ApiModelProperty("入驻时间")
    @TableField("t_merchant_set_create_time")
    @Column(name = "t_merchant_set_create_time",type = MySqlTypeConstant.DATETIME,isNull = true,comment = "入驻时间")
    private Date createTime;
}
