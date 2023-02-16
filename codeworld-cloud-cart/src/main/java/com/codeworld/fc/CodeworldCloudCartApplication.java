package com.codeworld.fc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.gitee.sunchenbin.mybatis.actable.manager.*","com.codeworld.fc.cart","com.codeworld.fc.common"})
@MapperScan(basePackages = {
        "com.gitee.sunchenbin.mybatis.actable.dao.*"
})
@EnableDiscoveryClient
@EnableFeignClients
public class CodeworldCloudCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeworldCloudCartApplication.class, args);
    }

}
