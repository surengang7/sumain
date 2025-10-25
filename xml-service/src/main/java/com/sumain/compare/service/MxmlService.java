package com.sumain.compare.service;


import com.sumain.compare.config.XmlBaseConfig;
import com.sumain.compare.model.GeneralException;
import com.sumain.compare.model.XmlCompareReport;
import com.sumain.compare.util.Dom4jUtils;
import com.sumain.compare.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MxmlService {

    @Resource
    private XmlBaseConfig baseConfig;


    public List<XmlCompareReport> compareByUpload(MultipartFile sourceFile,MultipartFile targetFile) throws IOException {
        if (sourceFile.isEmpty() || targetFile.isEmpty()) {
            return null;
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
        return result;
    }


    public void comparedBatchByPath(String sourceDir,String targetDir,String reportDir){
        // 读取source,target下所有文件
        if(reportDir == null || reportDir.trim().isEmpty()) reportDir = baseConfig.getReportDir();
        if(sourceDir == null || sourceDir.trim().isEmpty()) sourceDir = baseConfig.getSourceDir();
        if(targetDir == null || targetDir.trim().isEmpty()) targetDir = baseConfig.getTargetDir();
        Map<String, File> sourceFileMap = FileUtil.readDir(sourceDir);
        Map<String, File> targetFileMap = FileUtil.readDir(targetDir);


        if(sourceFileMap.isEmpty() || targetFileMap.isEmpty()){
            throw new GeneralException("目录为空或不存在");
        }

        // 根据名称遍历所有文件
        for (String fileName : sourceFileMap.keySet()) {
            File sourceFile = sourceFileMap.get(fileName);
            File targetFile = targetFileMap.get(fileName);
            if(targetFile != null){
                // 比较同名文件
                List<XmlCompareReport> result = Dom4jUtils.compare(sourceFile, targetFile);


                // 将结果输出到report 目录下
                String baseName = fileName.contains(".")
                        ? fileName.substring(0, fileName.lastIndexOf('.'))
                        : fileName;
                String resultPath = reportDir+"/"+baseName+".csv";
                FileUtil.writeObjectsToCsv(result,resultPath);

                // 移动原文件
                FileUtil.moveFileToDir(sourceFile, baseConfig.getSourceComparedDir());
                FileUtil.moveFileToDir(targetFile, baseConfig.getTargetComparedDir());
            }
        }

    }




}
