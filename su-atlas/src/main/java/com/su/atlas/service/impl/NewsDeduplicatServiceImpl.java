package com.su.atlas.service.impl;

import com.su.atlas.mapper.NewsDeduplicatMapper;
import com.su.atlas.service.NewsDeduplicatService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class NewsDeduplicatServiceImpl implements NewsDeduplicatService {

    @Resource
    private NewsDeduplicatMapper baseMapper;


}
