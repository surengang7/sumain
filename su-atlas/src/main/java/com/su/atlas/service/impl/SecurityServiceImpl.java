package com.su.atlas.service.impl;

import com.su.atlas.entity.Security;
import com.su.atlas.exception.GeneralException;
import com.su.atlas.mapper.SecurityMapper;
import com.su.atlas.service.SecurityService;
import com.su.common.utils.ExcelUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class SecurityServiceImpl implements SecurityService {

    @Resource
    private SecurityMapper baseMapper;


    @Override
    public void saveFromExcel(MultipartFile file) throws IOException {
        // 获取文件
        if(file == null){
            throw new GeneralException("上传的文件不能为空");
        }
        File securityExcel = File.createTempFile("security", ".xls");
        file.transferTo(securityExcel);


        // 解析文件
        Map<String, String> securityMap = ExcelUtils.read(securityExcel, true);


        // 组装实体
        Security security = new Security();


        // 删除临时文件
        @SuppressWarnings("unused")
        boolean delete = securityExcel.delete();
    }
}
