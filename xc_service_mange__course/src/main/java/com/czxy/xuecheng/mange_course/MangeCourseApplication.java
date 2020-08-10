package com.czxy.xuecheng.mange_course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("com.czxy.xuecheng.domain.course")
@ComponentScan(basePackages = "com.czxy.xuecheng.common")
@ComponentScan(basePackages = "com.czxy.xuecheng.api")
@ComponentScan(basePackages = "com.czxy.xuecheng.utils")
@ComponentScan(basePackages = "com.czxy.xuecheng.mange_course")
@EnableEurekaClient
@EnableFeignClients
public class MangeCourseApplication {
    public static void main(String[] args) {
        SpringApplication.run(MangeCourseApplication.class,args);
    }
}
