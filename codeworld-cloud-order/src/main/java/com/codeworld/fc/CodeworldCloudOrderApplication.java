package com.codeworld.fc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextListener;

@SpringBootApplication(scanBasePackages = {"com.gitee.sunchenbin.mybatis.actable.manager.*"})
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan(basePackages = {
        "com.gitee.sunchenbin.mybatis.actable.dao.*"
})
public class CodeworldCloudOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeworldCloudOrderApplication.class, args);
    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }

}
