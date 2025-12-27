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
    private CashSnapshotMapper cashSnapshotMapper;

    @Override
    public List<CashSnapshot> selectAll() {
        return cashSnapshotMapper.selectAll();
    }

    @Override
    public CashSnapshot selectById(Long id) {
        return cashSnapshotMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return cashSnapshotMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(CashSnapshot entity) {
        return cashSnapshotMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(CashSnapshot entity) {
        return cashSnapshotMapper.insert(entity) > 0;
    }

}
