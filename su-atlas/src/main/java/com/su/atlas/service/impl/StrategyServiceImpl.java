package com.su.atlas.service.impl;

import com.su.atlas.mapper.StrategyMapper;
import com.su.atlas.service.StrategyService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class StrategyServiceImpl implements StrategyService {

    @Resource
    private StrategyMapper baseMapper;

}
