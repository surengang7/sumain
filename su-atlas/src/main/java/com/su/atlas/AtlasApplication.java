package com.su.atlas;


import com.su.atlas.service.ExchangeService;
import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.su.atlas.mapper")
public class AtlasApplication implements ApplicationRunner {

    @Resource
    private ExchangeService exchangeService;

    public static void main(String[] args) {
        SpringApplication.run(AtlasApplication.class,args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // init exchange
        exchangeService.init();

        // init
    }
}
