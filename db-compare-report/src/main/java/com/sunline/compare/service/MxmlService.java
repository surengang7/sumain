package com.sunline.compare.service;


import com.sunline.compare.model.XmlCompareReport;
import com.sunline.compare.util.Dom4jUtils;
import com.sunline.compare.util.FileUtil;
import com.sunline.compare.util.XmlUnitUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import java.io.File;
import java.util.List;

@Service
@Slf4j
public class MxmlService {

    public void compareByDom4j() {
        String sourcePath = "D:\\MyProject\\sumain\\db-compare-report\\Mxml\\source\\source.xml";
        String targetPath = "D:\\MyProject\\sumain\\db-compare-report\\Mxml\\target\\target.xml";
        List<XmlCompareReport> result = Dom4jUtils.compare(new File(sourcePath), new File(targetPath));
        String csvPath = "D:\\MyProject\\sumain\\db-compare-report\\Mxml\\report\\report_dom4j.csv";
        FileUtil.writeObjectsToCsv(result,csvPath);
    }


    public void compareByXmlUnit() {
        String sourcePath = "D:\\MyProject\\sumain\\db-compare-report\\Mxml\\source\\source.xml";
        String targetPath = "D:\\MyProject\\sumain\\db-compare-report\\Mxml\\target\\target.xml";
        File sourceFile = new File(sourcePath);
        File targetFile = new File(targetPath);
        List<XmlCompareReport> xmlDiffResults = XmlUnitUtils.compareXml(sourceFile, targetFile);
        String csvPath = "D:\\MyProject\\sumain\\db-compare-report\\Mxml\\report\\report_xmlunit.csv";
        FileUtil.writeObjectsToCsv(xmlDiffResults,csvPath);
    }


    public List<XmlCompareReport> compareByDom4j(String source, String target, String xpath) {
        Element sourceElement;
        Element targetElement;
        try {
            sourceElement = XmlUnitUtils.getElementByXPath(source, xpath);
            targetElement = XmlUnitUtils.getElementByXPath(target, xpath);
        } catch (Exception e) {
            log.error("无效的xpath,源文件或目标文件不存在此节点");
            throw new RuntimeException(e.getMessage());
        }
        if (sourceElement != null && targetElement != null) {
            return XmlUnitUtils.compareXml(sourceElement, targetElement);
        }
        return null;
    }


}
