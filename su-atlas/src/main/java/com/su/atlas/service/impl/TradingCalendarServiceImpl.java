package com.su.atlas.service.impl;

import com.su.atlas.mapper.TradingCalendarMapper;
import com.su.atlas.service.TradingCalendarService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class TradingCalendarServiceImpl implements TradingCalendarService {

    @Resource
    private TradingCalendarMapper baseMapper;


}
