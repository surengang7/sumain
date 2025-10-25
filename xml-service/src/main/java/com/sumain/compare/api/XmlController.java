package com.sumain.compare.api;


import com.sumain.compare.model.ResponseEntity;
import com.sumain.compare.model.XmlCompareReport;
import com.sumain.compare.service.MxmlService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/main/xml")
public class XmlController {

    @Resource
    private MxmlService mxmlService;

    @GetMapping("/compare/html")
    public String comparePage() {
        return "compare"; // 对应 resources/templates/compare.html
    }

    // single compare
    @PostMapping(value = "/compare/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<List<XmlCompareReport>> compareByUpload(
            @RequestParam("sourceFile") MultipartFile sourceFile,
            @RequestParam("targetFile") MultipartFile targetFile) throws IOException {
        return ResponseEntity.ok(mxmlService.compareByUpload(sourceFile,targetFile));
    }

    // batch compare
    @PostMapping(value = "/compare/batch")
    @ResponseBody
    public ResponseEntity<String> compareByUpload(
            @RequestParam(value = "sourceDir", required = false) String sourceDir,
            @RequestParam(value = "targetDir", required = false) String targetDir,
            @RequestParam(value = "reportDir", required = false) String reportDir){
        mxmlService.comparedBatchByPath(sourceDir,targetDir,reportDir);
        return ResponseEntity.ok();
    }

}
