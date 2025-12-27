package com.su.atlas.service.impl;

import com.su.atlas.mapper.SecurityMapper;
import com.su.atlas.service.SecurityService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Resource
    private SecurityMapper baseMapper;


}
