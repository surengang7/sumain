package com.su.atlas.service.impl;

import com.su.atlas.mapper.NewsItemMapper;
import com.su.atlas.service.NewsItemService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class NewsItemServiceImpl implements NewsItemService {

    @Resource
    private NewsItemMapper baseMapper;


}
