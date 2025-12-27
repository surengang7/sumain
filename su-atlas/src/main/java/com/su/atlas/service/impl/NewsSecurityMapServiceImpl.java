package com.su.atlas.service.impl;

import com.su.atlas.mapper.NewsSecurityMapMapper;
import com.su.atlas.service.NewsSecurityMapService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class NewsSecurityMapServiceImpl implements NewsSecurityMapService {

    @Resource
    private NewsSecurityMapMapper baseMapper;

}
