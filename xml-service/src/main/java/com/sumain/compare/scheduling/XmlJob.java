package com.sumain.compare.scheduling;

import com.sumain.compare.config.XmlBaseConfig;
import com.sumain.compare.service.MxmlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Slf4j
public class XmlJob {

    @Resource
    private XmlBaseConfig xmlBaseConfig;

    @Resource
    private MxmlService mxmlService;

    @Scheduled(fixedRate = 10000)
    public void xmlCompareJob(){
        if(xmlBaseConfig.getSchedulingEnable()){
            log.info("xml compare job started");
            mxmlService.comparedBatchByPath(xmlBaseConfig.getSourceDir(), xmlBaseConfig.getTargetDir(), xmlBaseConfig.getReportDir());
        }
    }
}
