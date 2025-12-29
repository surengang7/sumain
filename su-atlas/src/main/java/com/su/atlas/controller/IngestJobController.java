package com.su.atlas.controller;

import com.su.atlas.view.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 采集任务定义（定时/增量/全量） 前端控制器
 * </p>
 *
 * @author surengang
 * @since 2025-12-27
 */
@Controller
@RequestMapping("/atlas/ingestJob")
@Tag(name = "数据采集")
public class IngestJobController {

    @PutMapping("/exchange")
    @Operation(summary = "交易所",description = "交易所数据采集")
    public ResponseEntity<String> ingestExchangeInfo(){
        return ResponseEntity.ok();
    }
}
