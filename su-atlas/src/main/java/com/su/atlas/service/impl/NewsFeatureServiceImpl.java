package com.su.atlas.service.impl;

import com.su.atlas.entity.NewsFeature;
import com.su.atlas.mapper.NewsFeatureMapper;
import com.su.atlas.service.NewsFeatureService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NewsFeatureServiceImpl implements NewsFeatureService {

    @Resource
    private NewsFeatureMapper newsFeatureMapper;

    @Override
    public List<NewsFeature> selectAll() {
        return newsFeatureMapper.selectAll();
    }

    @Override
    public NewsFeature selectById(UUID id) {
        return newsFeatureMapper.selectById(id);
    }

    @Override
    public boolean deleteById(UUID id) {
        return newsFeatureMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(NewsFeature entity) {
        return newsFeatureMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(NewsFeature entity) {
        return newsFeatureMapper.insert(entity) > 0;
    }

}
