package com.su.atlas.service.impl;

import com.su.atlas.entity.MarketFactsDaily;
import com.su.atlas.mapper.MarketFactsDailyMapper;
import com.su.atlas.service.MarketFactsDailyService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MarketFactsDailyServiceImpl implements MarketFactsDailyService {

    @Resource
    private MarketFactsDailyMapper marketFactsDailyMapper;

    @Override
    public List<MarketFactsDaily> selectAll() {
        return marketFactsDailyMapper.selectAll();
    }

    @Override
    public MarketFactsDaily selectById(Long id) {
        return marketFactsDailyMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return marketFactsDailyMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(MarketFactsDaily entity) {
        return marketFactsDailyMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(MarketFactsDaily entity) {
        return marketFactsDailyMapper.insert(entity) > 0;
    }

}
