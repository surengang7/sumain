package com.su.atlas.service;

import com.su.atlas.entity.MarketFactsDaily;
import java.util.List;

public interface MarketFactsDailyService {

    List<MarketFactsDaily> selectAll();

    MarketFactsDaily selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(MarketFactsDaily entity);

    boolean insert(MarketFactsDaily entity);

}
