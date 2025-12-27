package com.su.atlas.service.impl;

import com.su.atlas.entity.NewsItem;
import com.su.atlas.mapper.NewsItemMapper;
import com.su.atlas.service.NewsItemService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NewsItemServiceImpl implements NewsItemService {

    @Resource
    private NewsItemMapper newsItemMapper;

    @Override
    public List<NewsItem> selectAll() {
        return newsItemMapper.selectAll();
    }

    @Override
    public NewsItem selectById(UUID id) {
        return newsItemMapper.selectById(id);
    }

    @Override
    public boolean deleteById(UUID id) {
        return newsItemMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(NewsItem entity) {
        return newsItemMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(NewsItem entity) {
        return newsItemMapper.insert(entity) > 0;
    }

}
