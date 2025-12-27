package com.su.atlas.service.impl;

import com.su.atlas.mapper.SecurityTagMapper;
import com.su.atlas.service.SecurityTagService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SecurityTagServiceImpl implements SecurityTagService {

    @Resource
    private SecurityTagMapper baseMapper;


}
