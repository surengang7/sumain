package com.su.atlas.service.impl;

import com.su.atlas.entity.CashSnapshot;
import com.su.atlas.mapper.CashSnapshotMapper;
import com.su.atlas.service.CashSnapshotService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CashSnapshotServiceImpl implements CashSnapshotService {

    @Resource
    private CashSnapshotMapper baseMapper;


}
