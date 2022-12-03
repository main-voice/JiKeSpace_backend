package com.tjsse.jikespace;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@MapperScan("com.tjsse.jikespace.mapper")
@SpringBootApplication
public class JiKeSpaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiKeSpaceApplication.class, args);
    }

}
