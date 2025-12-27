package com.su.atlas.service.impl;

import com.su.atlas.entity.FactorValue;
import com.su.atlas.mapper.FactorValueMapper;
import com.su.atlas.service.FactorValueService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FactorValueServiceImpl implements FactorValueService {

    @Resource
    private FactorValueMapper factorValueMapper;

    @Override
    public List<FactorValue> selectAll() {
        return factorValueMapper.selectAll();
    }

    @Override
    public FactorValue selectById(Long id) {
        return factorValueMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return factorValueMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(FactorValue entity) {
        return factorValueMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(FactorValue entity) {
        return factorValueMapper.insert(entity) > 0;
    }

}
