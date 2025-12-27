package com.su.atlas.service.impl;

import com.su.atlas.mapper.AdjFactorMapper;
import com.su.atlas.service.AdjFactorService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AdjFactorServiceImpl implements AdjFactorService {

    @Resource
    private AdjFactorMapper baseMapper;


}
