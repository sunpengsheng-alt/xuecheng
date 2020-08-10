package com.czxy.xuecheng.filesystem;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan("com.czxy.xuecheng.domain.filesystem")
@ComponentScan("com.czxy.xuecheng.api")
@ComponentScan("com.czxy.xuecheng.common")
@ComponentScan(basePackages={"com.czxy.xuecheng.filesystem"})
public class FileSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileSystemApplication.class,args);
    }
}
