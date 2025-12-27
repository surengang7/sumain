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
    private DataSourceMapper dataSourceMapper;

    @Override
    public List<DataSource> selectAll() {
        return dataSourceMapper.selectAll();
    }

    @Override
    public DataSource selectById(Long id) {
        return dataSourceMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return dataSourceMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(DataSource entity) {
        return dataSourceMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(DataSource entity) {
        return dataSourceMapper.insert(entity) > 0;
    }

}
