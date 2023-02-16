package com.codeworld.fc.auth.client.impl;

import com.codeworld.fc.auth.client.UserClient;
import com.codeworld.fc.auth.domain.User;
import com.codeworld.fc.common.enums.HttpFcStatus;
import com.codeworld.fc.common.enums.HttpMsg;
import com.codeworld.fc.common.response.FCResponse;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * ClassName UserClientFallBack
 * Description TODO
 * Author Lenovo
 * Date 2021/1/20
 * Version 1.0
**/
@Component
@Slf4j
public class UserClientFallBack implements FallbackFactory<UserClient> {

//    @Override
//    public FCResponse<User> getUserByName(String username) {
//        return FCResponse.dataResponse(HttpFcStatus.RUNTIMECODE.getCode(),"请求超时",null);
//    }
//
//    @Override
//    public FCResponse<User> getUserById(Long userId) {
//        return FCResponse.dataResponse(HttpFcStatus.RUNTIMECODE.getCode(),"请求超时",null);
//    }


    @Override
    public UserClient create(Throwable cause) {

       return new UserClient() {
           @Override
           public FCResponse<User> getUserByName(String username) {
               log.info("断路器启动------->请求参数为：{}",username);
               return FCResponse.dataResponse(HttpFcStatus.FEIGNFAIL.getCode(), HttpMsg.http.FEIGN_CLIENT_FAIL.getMsg()+"=======>"+cause.getMessage());
           }

           @Override
           public FCResponse<User> getUserById(Long userId) {
               log.info("断路器启动------->请求参数为：{}",userId);
               return FCResponse.dataResponse(HttpFcStatus.FEIGNFAIL.getCode(),HttpMsg.http.FEIGN_CLIENT_FAIL.getMsg()+"=======>"+cause.getMessage());
           }
       };




    }
}
