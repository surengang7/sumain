package com.su.atlas.service;

import com.su.atlas.entity.StrategyRun;
import java.util.List;

public interface StrategyRunService {

    List<StrategyRun> selectAll();

    StrategyRun selectById(UUID id);

    boolean deleteById(UUID id);

    boolean updateById(StrategyRun entity);

    boolean insert(StrategyRun entity);

}
