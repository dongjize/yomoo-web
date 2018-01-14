package com.yomoo.yomooweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
//@MapperScan(basePackages = "com.yomoo.yomooweb.mapper")
public class YomooApplication {
    public static void main(String[] args) {
        SpringApplication.run(YomooApplication.class, args);
    }
}
