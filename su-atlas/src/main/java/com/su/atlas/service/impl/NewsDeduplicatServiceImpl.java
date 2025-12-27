package com.su.atlas.service.impl;

import com.su.atlas.entity.NewsDeduplicat;
import com.su.atlas.mapper.NewsDeduplicatMapper;
import com.su.atlas.service.NewsDeduplicatService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NewsDeduplicatServiceImpl implements NewsDeduplicatService {

    @Resource
    private NewsDeduplicatMapper newsDeduplicatMapper;

    @Override
    public List<NewsDeduplicat> selectAll() {
        return newsDeduplicatMapper.selectAll();
    }

    @Override
    public boolean insert(NewsDeduplicat entity) {
        return newsDeduplicatMapper.insert(entity) > 0;
    }

}
