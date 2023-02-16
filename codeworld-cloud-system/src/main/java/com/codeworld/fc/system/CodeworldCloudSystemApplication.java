package com.codeworld.fc.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication(scanBasePackages = {"com.gitee.sunchenbin.mybatis.actable.manager.*","com.codeworld.fc.system.*","com.codeworld.fc.common"})
@EnableDiscoveryClient
@MapperScan(basePackages = {
        "com.gitee.sunchenbin.mybatis.actable.dao.*"
})
public class CodeworldCloudSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(CodeworldCloudSystemApplication.class, args);
    }

}
