package com.su.atlas.service.impl;

import com.su.atlas.mapper.RawPayloadMapper;
import com.su.atlas.service.RawPayloadService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class RawPayloadServiceImpl implements RawPayloadService {

    @Resource
    private RawPayloadMapper baseMapper;


}
