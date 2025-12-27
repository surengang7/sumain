package com.su.atlas.service.impl;

import com.su.atlas.entity.RiskEvent;
import com.su.atlas.mapper.RiskEventMapper;
import com.su.atlas.service.RiskEventService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RiskEventServiceImpl implements RiskEventService {

    @Resource
    private RiskEventMapper riskEventMapper;

    @Override
    public List<RiskEvent> selectAll() {
        return riskEventMapper.selectAll();
    }

    @Override
    public RiskEvent selectById(Long id) {
        return riskEventMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return riskEventMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(RiskEvent entity) {
        return riskEventMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(RiskEvent entity) {
        return riskEventMapper.insert(entity) > 0;
    }

}
