package com.su.atlas.service.impl;

import com.su.atlas.mapper.MarketFactsMinuteMapper;
import com.su.atlas.service.MarketFactsMinuteService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class MarketFactsMinuteServiceImpl implements MarketFactsMinuteService {

    @Resource
    private MarketFactsMinuteMapper baseMapper;


}
