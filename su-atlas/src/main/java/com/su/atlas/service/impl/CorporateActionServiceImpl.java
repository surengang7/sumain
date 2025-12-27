package com.su.atlas.service.impl;

import com.su.atlas.mapper.CorporateActionMapper;
import com.su.atlas.service.CorporateActionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CorporateActionServiceImpl implements CorporateActionService {

    @Resource
    private CorporateActionMapper baseMapper;


}
