package com.zfile;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zfile.module.**.mapper")
public class ZFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZFileApplication.class, args);
    }

}