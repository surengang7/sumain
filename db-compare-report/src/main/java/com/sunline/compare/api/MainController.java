package com.sunline.compare.api;


import com.sunline.compare.model.XmlCompareReport;
import com.sunline.compare.service.MxmlService;
import com.sunline.compare.util.Dom4jUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/main/xml")
public class MainController {

    @Resource
    private MxmlService mxmlService;

    @GetMapping("/compare/html")
    public String comparePage() {
        return "compare"; // 对应 resources/templates/compare.html
    }

    // 接口上传文件并比较
    @PostMapping(value = "/compare/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<List<XmlCompareReport>> compareByUpload(
            @RequestParam("sourceFile") MultipartFile sourceFile,
            @RequestParam("targetFile") MultipartFile targetFile) throws IOException {

        if (sourceFile.isEmpty() || targetFile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 保存临时文件
        File sourceTmp = File.createTempFile("source", ".xml");
        File targetTmp = File.createTempFile("target", ".xml");
        sourceFile.transferTo(sourceTmp);
        targetFile.transferTo(targetTmp);

        List<XmlCompareReport> result = Dom4jUtils.compare(sourceTmp, targetTmp);

        // 删除临时文件
        @SuppressWarnings("unused")
        boolean delete = sourceTmp.delete();
        @SuppressWarnings("unused")
        boolean delete1 = targetTmp.delete();

        return ResponseEntity.ok(result);
    }


    @GetMapping("/compare/dom4j")
    public String compareByDom4j(){
        mxmlService.compareByDom4j();
        return "success";
    }

    @GetMapping("/compare/xmlunit")
    public String compareByXmlUnit(){
        mxmlService.compareByXmlUnit();
        return "success";
    }

}
