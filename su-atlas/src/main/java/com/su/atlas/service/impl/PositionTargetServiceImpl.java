package com.su.atlas.service.impl;

import com.su.atlas.entity.PositionTarget;
import com.su.atlas.mapper.PositionTargetMapper;
import com.su.atlas.service.PositionTargetService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PositionTargetServiceImpl implements PositionTargetService {

    @Resource
    private PositionTargetMapper positionTargetMapper;

    @Override
    public List<PositionTarget> selectAll() {
        return positionTargetMapper.selectAll();
    }

    @Override
    public boolean insert(PositionTarget entity) {
        return positionTargetMapper.insert(entity) > 0;
    }

}
