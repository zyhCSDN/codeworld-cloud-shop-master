package com.codeworld.fc.goods.client.impl;

import com.codeworld.fc.common.enums.HttpFcStatus;
import com.codeworld.fc.common.response.FCResponse;
import com.codeworld.fc.goods.client.UserClient;
import com.codeworld.fc.goods.domain.User;
import org.springframework.stereotype.Component;

/**
 * ClassName UserClientFallBack
 * Description TODO
 * Author Lenovo
 * Date 2021/1/20
 * Version 1.0
**/
@Component
public class UserClientFallBack implements UserClient {

    @Override
    public FCResponse<User> getUserById(Long userId) {
        return FCResponse.dataResponse(HttpFcStatus.RUNTIMECODE.getCode(),"请求超时",null);
    }
}
