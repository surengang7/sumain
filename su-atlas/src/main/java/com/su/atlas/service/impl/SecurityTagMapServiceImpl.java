package com.su.atlas.service.impl;

import com.su.atlas.mapper.SecurityTagMapMapper;
import com.su.atlas.service.SecurityTagMapService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SecurityTagMapServiceImpl implements SecurityTagMapService {

    @Resource
    private SecurityTagMapMapper baseMapper;


}
