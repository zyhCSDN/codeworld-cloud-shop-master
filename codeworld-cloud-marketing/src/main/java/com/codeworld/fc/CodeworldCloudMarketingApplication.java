package com.codeworld.fc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.codeworld.fc.marketing.*"})
@MapperScan(basePackages = {
        "com.codeworld.fc.marketing.carouse.mapper"
})
@EnableDiscoveryClient
@EnableFeignClients
public class CodeworldCloudMarketingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeworldCloudMarketingApplication.class, args);
    }

}
