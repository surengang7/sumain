package com.su.atlas.service.impl;


import com.su.atlas.mapper.TradeFillMapper;
import com.su.atlas.service.TradeFillService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class TradeFillServiceImpl implements TradeFillService {

    @Resource
    private TradeFillMapper baseMapper;


}
