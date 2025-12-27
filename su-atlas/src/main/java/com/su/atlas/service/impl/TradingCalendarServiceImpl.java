package com.su.atlas.service.impl;

import com.su.atlas.entity.TradingCalendar;
import com.su.atlas.mapper.TradingCalendarMapper;
import com.su.atlas.service.TradingCalendarService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TradingCalendarServiceImpl implements TradingCalendarService {

    @Resource
    private TradingCalendarMapper tradingCalendarMapper;

    @Override
    public List<TradingCalendar> selectAll() {
        return tradingCalendarMapper.selectAll();
    }

    @Override
    public boolean insert(TradingCalendar entity) {
        return tradingCalendarMapper.insert(entity) > 0;
    }

}
