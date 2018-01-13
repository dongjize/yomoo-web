package com.yomoo.yomooweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
//@MapperScan(basePackages = "com.yomoo.yomooweb.mapper")
public class YomooApplication {

	public static void main(String[] args) {
		SpringApplication.run(YomooApplication.class, args);
	}
}
