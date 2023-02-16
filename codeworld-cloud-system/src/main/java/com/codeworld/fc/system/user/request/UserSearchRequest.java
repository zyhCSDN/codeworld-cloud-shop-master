package com.codeworld.fc.system.user.request;

import com.codeworld.fc.common.base.PageQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * ClassName UserSearchRequest
 * Description 用户搜索Request
 * Author Lenovo
 * Date 2020/10/29
 * Version 1.0
 **/
@Data
@ApiModel("用户搜索Request")
public class UserSearchRequest extends PageQuery {

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户邮箱")
    private String userEmail;

    @ApiModelProperty("用户手机")
    private String userPhone;

    @ApiModelProperty("用户状态")
    private Integer userStatus;

    @ApiModelProperty("区域Id")
    private Long areaId;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", locale = "zh")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8", locale = "zh")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ApiModelProperty("角色Id")
    private Long roleId;

}
