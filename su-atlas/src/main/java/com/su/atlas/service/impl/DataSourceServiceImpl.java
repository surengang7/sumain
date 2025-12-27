package com.su.atlas.service.impl;

import com.su.atlas.entity.DataSource;
import com.su.atlas.mapper.DataSourceMapper;
import com.su.atlas.service.DataSourceService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DataSourceServiceImpl implements DataSourceService {

    @Resource
    private DataSourceMapper baseMapper;


}
