package com.su.atlas.service.impl;

import com.su.atlas.entity.IngestJob;
import com.su.atlas.mapper.IngestJobMapper;
import com.su.atlas.service.IngestJobService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IngestJobServiceImpl implements IngestJobService {

    @Resource
    private IngestJobMapper ingestJobMapper;

    @Override
    public List<IngestJob> selectAll() {
        return ingestJobMapper.selectAll();
    }

    @Override
    public IngestJob selectById(Long id) {
        return ingestJobMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return ingestJobMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(IngestJob entity) {
        return ingestJobMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(IngestJob entity) {
        return ingestJobMapper.insert(entity) > 0;
    }

}
