package com.su.atlas.service.impl;

import com.su.atlas.mapper.IngestJobMapper;
import com.su.atlas.service.IngestJobService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class IngestJobServiceImpl implements IngestJobService {

    @Resource
    private IngestJobMapper baseMapper;


}
