package com.su.atlas.service.impl;

import com.su.atlas.entity.Strategy;
import com.su.atlas.mapper.StrategyMapper;
import com.su.atlas.service.StrategyService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StrategyServiceImpl implements StrategyService {

    @Resource
    private StrategyMapper strategyMapper;

    @Override
    public List<Strategy> selectAll() {
        return strategyMapper.selectAll();
    }

    @Override
    public Strategy selectById(Long id) {
        return strategyMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return strategyMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(Strategy entity) {
        return strategyMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(Strategy entity) {
        return strategyMapper.insert(entity) > 0;
    }

}
