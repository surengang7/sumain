package com.su.atlas.service;

import com.su.atlas.entity.RiskEvent;
import java.util.List;

public interface RiskEventService {

    List<RiskEvent> selectAll();

    RiskEvent selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(RiskEvent entity);

    boolean insert(RiskEvent entity);

}
