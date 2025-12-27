package com.su.atlas.service.impl;

import com.su.atlas.mapper.PositionSnapshotMapper;
import com.su.atlas.service.PositionSnapshotService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PositionSnapshotServiceImpl implements PositionSnapshotService {

    @Resource
    private PositionSnapshotMapper baseMapper;


}
