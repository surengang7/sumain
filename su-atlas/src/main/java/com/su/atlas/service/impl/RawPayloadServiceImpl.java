package com.su.atlas.service.impl;

import com.su.atlas.entity.RawPayload;
import com.su.atlas.mapper.RawPayloadMapper;
import com.su.atlas.service.RawPayloadService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RawPayloadServiceImpl implements RawPayloadService {

    @Resource
    private RawPayloadMapper rawPayloadMapper;

    @Override
    public List<RawPayload> selectAll() {
        return rawPayloadMapper.selectAll();
    }

    @Override
    public RawPayload selectById(UUID id) {
        return rawPayloadMapper.selectById(id);
    }

    @Override
    public boolean deleteById(UUID id) {
        return rawPayloadMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(RawPayload entity) {
        return rawPayloadMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(RawPayload entity) {
        return rawPayloadMapper.insert(entity) > 0;
    }

}
