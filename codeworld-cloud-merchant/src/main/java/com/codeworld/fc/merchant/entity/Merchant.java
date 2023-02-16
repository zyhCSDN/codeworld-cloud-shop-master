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
 * ClassName Merchant
 * Description 商户Model
 * Author Lenovo
 * Date 2020/12/30
 * Version 1.0
**/
@TableName("codeworld_merchant")
@Table(name = "codeworld_merchant", comment = "商户Model")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("商户Model")
public class Merchant {

    @ApiModelProperty("商户Id")
    @TableField("t_merchant_id")
    @Column(name = "t_merchant_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "商户id")
    private Long id;

    @ApiModelProperty("商户号")
    @TableField("t_merchant_number")
    @Column(name = "t_merchant_number",type = MySqlTypeConstant.VARCHAR,length = 20,isNull = false,comment = "商户号")
    private String number;

    @ApiModelProperty("商户昵称")
    @TableField("t_merchant_nick_name")
    @Column(name = "t_merchant_nick_name",type = MySqlTypeConstant.VARCHAR,length = 10,isNull = false,comment = "商户昵称")
    private String nickName;

    @ApiModelProperty("头像")
    @TableField("t_merchant_avatar")
    @Column(name = "t_merchant_avatar",type = MySqlTypeConstant.VARCHAR,length = 1000,isNull = true,comment = "商户头像")
    private String avatar;

    @ApiModelProperty("商户手机号")
    @TableField("t_merchant_phone")
    @Column(name = "t_merchant_phone",type = MySqlTypeConstant.VARCHAR,length = 11,isNull = false,comment = "商户手机")
    private String phone;

    @ApiModelProperty("商户登录密码")
    @TableField("t_merchant_password")
    @Column(name = "t_merchant_password",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "商户登陆密码")
    private String password;

    @ApiModelProperty("创建时间")
    @TableField("t_merchant_create_time")
    @Column(name = "t_merchant_create_time",type = MySqlTypeConstant.DATETIME,isNull = false,comment = "创建时间")
    private Date createTime;

    @ApiModelProperty("商户跟进人")
    @TableField("t_merchant_follow_user")
    @Column(name = "t_merchant_follow_user",type = MySqlTypeConstant.BIGINT,length = 20,isNull = true,comment = "商户跟进人id")
    private Long merchantFollowUser;

    @ApiModelProperty("密码盐值")
    @TableField("t_merchant_password_salt")
    @Column(name = "t_merchant_password_salt",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "商户密码加盐salt")
    private String passwordSalt;
}
