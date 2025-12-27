package com.su.atlas.service.impl;

import com.su.atlas.mapper.MarketFactsDailyMapper;
import com.su.atlas.service.MarketFactsDailyService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class MarketFactsDailyServiceImpl implements MarketFactsDailyService {

    @Resource
    private MarketFactsDailyMapper baseMapper;


}
