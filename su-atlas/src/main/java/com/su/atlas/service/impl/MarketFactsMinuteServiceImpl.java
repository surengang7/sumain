package com.su.atlas.service.impl;

import com.su.atlas.entity.MarketFactsMinute;
import com.su.atlas.mapper.MarketFactsMinuteMapper;
import com.su.atlas.service.MarketFactsMinuteService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MarketFactsMinuteServiceImpl implements MarketFactsMinuteService {

    @Resource
    private MarketFactsMinuteMapper marketFactsMinuteMapper;

    @Override
    public List<MarketFactsMinute> selectAll() {
        return marketFactsMinuteMapper.selectAll();
    }

    @Override
    public MarketFactsMinute selectById(Long id) {
        return marketFactsMinuteMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return marketFactsMinuteMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(MarketFactsMinute entity) {
        return marketFactsMinuteMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(MarketFactsMinute entity) {
        return marketFactsMinuteMapper.insert(entity) > 0;
    }

}
