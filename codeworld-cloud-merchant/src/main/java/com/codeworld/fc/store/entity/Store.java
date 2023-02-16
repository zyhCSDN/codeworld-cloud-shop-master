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
 * ClassName Store
 * Description TODO
 * Author Lenovo
 * Date 2021/3/1
 * Version 1.0
**/
@TableName("codeworld_store")
@Table(name = "codeworld_store", comment = "店铺")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("店铺")
public class Store {

    @ApiModelProperty("店铺Id")
    @TableField("t_store_id")
    @Column(name = "t_store_id",type = MySqlTypeConstant.BIGINT,length = 20,isNull = false,comment = "店铺id")
    private Long id;

    @ApiModelProperty("店铺名")
    @TableField("t_store_name")
    @Column(name = "t_store_name",type = MySqlTypeConstant.VARCHAR,length = 255,isNull = false,comment = "店铺名称")
    private String name;

    @ApiModelProperty("状态")
    @TableField("t_store_status")
    @Column(name = "t_store_status",type = MySqlTypeConstant.INT,length = 1,isNull = false,comment = "店铺状态 1--启用中 2--未启用")
    private Integer status;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("t_store_create_time")
    @Column(name = "t_store_create_time",type = MySqlTypeConstant.DATETIME,isNull = false,comment = "创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("t_store_update_time")
    @Column(name = "t_store_update_time",type = MySqlTypeConstant.DATETIME,isNull = false,comment = "更新时间")
    private Date updateTime;

    @ApiModelProperty("商户号")
    @TableField("t_merchant_number")
    @Column(name = "t_merchant_number",type = MySqlTypeConstant.VARCHAR,length = 20,isNull = false,comment = "商户号")
    private String merchantNumber;
}
