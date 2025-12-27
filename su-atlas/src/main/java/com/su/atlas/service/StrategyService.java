package com.su.atlas.service;

import com.su.atlas.entity.Strategy;
import java.util.List;

public interface StrategyService {

    List<Strategy> selectAll();

    Strategy selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(Strategy entity);

    boolean insert(Strategy entity);

}
