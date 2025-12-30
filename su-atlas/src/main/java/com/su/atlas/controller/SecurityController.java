package com.su.atlas.controller;

import com.su.atlas.service.FileService;
import com.su.atlas.view.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 证券主表（股票/ETF/指数等统一抽象） 前端控制器
 * </p>
 *
 * @author surengang
 * @since 2025-12-27
 */
@RestController
@RequestMapping("/atlas/security")
@Tag(name = "security")
public class SecurityController {

    @Resource
    private FileService fileService;


    @PostMapping(value = "/import/xls", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "XLS Import",description = "通过xls文件导入股票基本信息")
    public ResponseEntity<String> importSecurity(@RequestPart("file") MultipartFile file){
        fileService.handlerMultipartFile(file);
        return ResponseEntity.ok();
    }

}
