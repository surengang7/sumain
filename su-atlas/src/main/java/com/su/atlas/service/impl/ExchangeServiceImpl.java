package com.su.atlas.service.impl;

import com.su.atlas.mapper.ExchangeMapper;
import com.su.atlas.service.ExchangeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Resource
    private ExchangeMapper baseMapper;


    @Override
    public void init() {

    }
}
