package com.su.atlas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 资讯全局去重索引表（跨分区保证 source+source_item_id 唯一） 前端控制器
 * </p>
 *
 * @author surengang
 * @since 2025-12-27
 */
@Controller
@RequestMapping("/atlas/newsDeduplicat")
public class NewsDeduplicatController {

}
