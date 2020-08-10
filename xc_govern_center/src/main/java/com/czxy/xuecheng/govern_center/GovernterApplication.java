package com.czxy.xuecheng.govern_center;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class GovernterApplication {
    public static void main(String[] args) {
        SpringApplication.run(GovernterApplication.class,args);
    }
}
