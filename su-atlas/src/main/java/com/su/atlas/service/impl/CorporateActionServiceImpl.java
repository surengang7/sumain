package com.su.atlas.service.impl;

import com.su.atlas.entity.CorporateAction;
import com.su.atlas.mapper.CorporateActionMapper;
import com.su.atlas.service.CorporateActionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CorporateActionServiceImpl implements CorporateActionService {

    @Resource
    private CorporateActionMapper corporateActionMapper;

    @Override
    public List<CorporateAction> selectAll() {
        return corporateActionMapper.selectAll();
    }

    @Override
    public CorporateAction selectById(Long id) {
        return corporateActionMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return corporateActionMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(CorporateAction entity) {
        return corporateActionMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(CorporateAction entity) {
        return corporateActionMapper.insert(entity) > 0;
    }

}
