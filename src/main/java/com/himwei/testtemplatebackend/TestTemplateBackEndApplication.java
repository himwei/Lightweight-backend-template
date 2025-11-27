package com.himwei.testtemplatebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.himwei.testtemplatebackend.mapper")
@SpringBootApplication
public class TestTemplateBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestTemplateBackEndApplication.class, args);
        System.out.println("启动成功");
    }

}
