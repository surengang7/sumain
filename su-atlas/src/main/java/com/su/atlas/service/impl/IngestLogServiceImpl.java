package com.su.atlas.service.impl;

import com.su.atlas.entity.IngestLog;
import com.su.atlas.mapper.IngestLogMapper;
import com.su.atlas.service.IngestLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IngestLogServiceImpl implements IngestLogService {

    @Resource
    private IngestLogMapper ingestLogMapper;

    @Override
    public List<IngestLog> selectAll() {
        return ingestLogMapper.selectAll();
    }

    @Override
    public IngestLog selectById(Long id) {
        return ingestLogMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return ingestLogMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(IngestLog entity) {
        return ingestLogMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(IngestLog entity) {
        return ingestLogMapper.insert(entity) > 0;
    }

}
