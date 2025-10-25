package com.sumain.compare.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@Data
@ConfigurationProperties(prefix = "xml.compare.xpath")
public class XmlBaseConfig {

    // path config
    @Value("${xml.compare.path.source:/murex/service/compare/xml/data/source}")
    private String sourceDir;

    @Value("${xml.compare.path.target:/murex/service/compare/xml/data/target}")
    private String targetDir;

    @Value("${xml.compare.path.source-compared:/murex/service/compare/xml/data/source-compared}")
    private String sourceComparedDir;

    @Value("${xml.compare.path.target-compared:/murex/service/compare/xml/data/target-compared}")
    private String targetComparedDir;

    @Value("${xml.compare.path.report:/murex/service/compare/xml/data/report}")
    private String reportDir;

    // scheduling config
    @Value("${xml.compare.scheduling.enable:false}")
    private Boolean schedulingEnable;

    // xpath config
    private List<String> exclude;

    private List<String> include;
}
