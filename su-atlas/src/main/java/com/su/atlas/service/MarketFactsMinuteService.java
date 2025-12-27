package com.su.atlas.service;

import com.su.atlas.entity.MarketFactsMinute;
import java.util.List;

public interface MarketFactsMinuteService {

    List<MarketFactsMinute> selectAll();

    MarketFactsMinute selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(MarketFactsMinute entity);

    boolean insert(MarketFactsMinute entity);

}
