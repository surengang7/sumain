package com.su.atlas.service.impl;

import com.su.atlas.entity.StrategyRun;
import com.su.atlas.mapper.StrategyRunMapper;
import com.su.atlas.service.StrategyRunService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StrategyRunServiceImpl implements StrategyRunService {

    @Resource
    private StrategyRunMapper strategyRunMapper;

    @Override
    public List<StrategyRun> selectAll() {
        return strategyRunMapper.selectAll();
    }

    @Override
    public StrategyRun selectById(UUID id) {
        return strategyRunMapper.selectById(id);
    }

    @Override
    public boolean deleteById(UUID id) {
        return strategyRunMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(StrategyRun entity) {
        return strategyRunMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(StrategyRun entity) {
        return strategyRunMapper.insert(entity) > 0;
    }

}
