package com.su.atlas.service.impl;

import com.su.atlas.mapper.RiskEventMapper;
import com.su.atlas.service.RiskEventService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RiskEventServiceImpl implements RiskEventService {

    @Resource
    private RiskEventMapper baseMapper;


}
