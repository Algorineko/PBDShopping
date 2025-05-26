package com.pbdcompany;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.pbdcompany.mapper")
public class WebShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebShopApplication.class, args);
    }

}
