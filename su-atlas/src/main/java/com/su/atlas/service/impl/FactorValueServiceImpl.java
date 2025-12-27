package com.su.atlas.service.impl;

import com.su.atlas.mapper.FactorValueMapper;
import com.su.atlas.service.FactorValueService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class FactorValueServiceImpl implements FactorValueService {

    @Resource
    private FactorValueMapper baseMapper;

}
