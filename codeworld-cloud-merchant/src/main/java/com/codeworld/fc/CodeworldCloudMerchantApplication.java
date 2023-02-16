package com.codeworld.fc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.gitee.sunchenbin.mybatis.actable.manager.*"})
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan(basePackages = {
        "com.gitee.sunchenbin.mybatis.actable.dao.*"
})
public class CodeworldCloudMerchantApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeworldCloudMerchantApplication.class, args);
    }

}
