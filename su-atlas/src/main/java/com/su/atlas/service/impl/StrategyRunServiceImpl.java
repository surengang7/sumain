package com.su.atlas.service.impl;

import com.su.atlas.mapper.StrategyRunMapper;
import com.su.atlas.service.StrategyRunService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class StrategyRunServiceImpl implements StrategyRunService {

    @Resource
    private StrategyRunMapper baseMapper;

}
