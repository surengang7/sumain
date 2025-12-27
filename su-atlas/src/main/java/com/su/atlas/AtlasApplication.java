package com.su.atlas;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.su.atlas.mapper")
public class AtlasApplication {


    public static void main(String[] args) {
        SpringApplication.run(AtlasApplication.class,args);
    }

}
