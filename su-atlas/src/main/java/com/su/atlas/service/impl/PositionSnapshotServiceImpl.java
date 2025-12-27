package com.su.atlas.service.impl;

import com.su.atlas.entity.PositionSnapshot;
import com.su.atlas.mapper.PositionSnapshotMapper;
import com.su.atlas.service.PositionSnapshotService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PositionSnapshotServiceImpl implements PositionSnapshotService {

    @Resource
    private PositionSnapshotMapper positionSnapshotMapper;

    @Override
    public List<PositionSnapshot> selectAll() {
        return positionSnapshotMapper.selectAll();
    }

    @Override
    public boolean insert(PositionSnapshot entity) {
        return positionSnapshotMapper.insert(entity) > 0;
    }

}
