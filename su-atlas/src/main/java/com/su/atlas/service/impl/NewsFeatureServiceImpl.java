package com.su.atlas.service.impl;

import com.su.atlas.mapper.NewsFeatureMapper;
import com.su.atlas.service.NewsFeatureService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


@Service
public class NewsFeatureServiceImpl implements NewsFeatureService {

    @Resource
    private NewsFeatureMapper baseMapper;

}
