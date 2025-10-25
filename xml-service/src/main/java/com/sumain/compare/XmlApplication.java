package com.sumain.compare;


import com.sumain.compare.config.XmlBaseConfig;
import com.sumain.compare.util.Dom4jUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;


@SpringBootApplication
@Slf4j
@EnableScheduling
public class XmlApplication implements ApplicationRunner {

    @Resource
    private XmlBaseConfig xmlBaseConfig;

    public static void main(String[] args) {
        SpringApplication.run(XmlApplication.class,args);
    }

    @Override
    public void run(ApplicationArguments args) {
        Dom4jUtils.excludeXpath.addAll(xmlBaseConfig.getExclude());
        Dom4jUtils.includeXpath.addAll(xmlBaseConfig.getInclude());
        log.info("xml compare application started success");
    }
}
