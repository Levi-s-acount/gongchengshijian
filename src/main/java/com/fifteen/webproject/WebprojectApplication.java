package com.fifteen.webproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.fifteen.webproject.mapper")
@SpringBootApplication
public class WebprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebprojectApplication.class, args);
    }

}
