package com.su.atlas.service.impl;

import com.su.atlas.entity.NewsSecurityMap;
import com.su.atlas.mapper.NewsSecurityMapMapper;
import com.su.atlas.service.NewsSecurityMapService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NewsSecurityMapServiceImpl implements NewsSecurityMapService {

    @Resource
    private NewsSecurityMapMapper newsSecurityMapMapper;

    @Override
    public List<NewsSecurityMap> selectAll() {
        return newsSecurityMapMapper.selectAll();
    }

    @Override
    public boolean insert(NewsSecurityMap entity) {
        return newsSecurityMapMapper.insert(entity) > 0;
    }

}
