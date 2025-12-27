package com.su.atlas.service.impl;

import com.su.atlas.mapper.PositionTargetMapper;
import com.su.atlas.service.PositionTargetService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PositionTargetServiceImpl implements PositionTargetService {

    @Resource
    private PositionTargetMapper baseMapper;


}
