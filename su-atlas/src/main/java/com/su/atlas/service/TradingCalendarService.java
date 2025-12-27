package com.su.atlas.service;

import com.su.atlas.entity.TradingCalendar;
import java.util.List;

public interface TradingCalendarService {

    List<TradingCalendar> selectAll();

    boolean insert(TradingCalendar entity);

    // 未检测到唯一主键（可能是复合主键表），这里只生成 insert/selectAll。
}
