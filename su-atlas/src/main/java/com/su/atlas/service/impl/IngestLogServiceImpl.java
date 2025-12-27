package com.su.atlas.service.impl;

import com.su.atlas.mapper.IngestLogMapper;
import com.su.atlas.service.IngestLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class IngestLogServiceImpl implements IngestLogService {

    @Resource
    private IngestLogMapper baseMapper;


}
