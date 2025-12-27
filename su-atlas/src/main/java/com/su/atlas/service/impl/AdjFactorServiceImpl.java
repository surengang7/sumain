package com.su.atlas.service.impl;

import com.su.atlas.entity.AdjFactor;
import com.su.atlas.mapper.AdjFactorMapper;
import com.su.atlas.service.AdjFactorService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdjFactorServiceImpl implements AdjFactorService {

    @Resource
    private AdjFactorMapper adjFactorMapper;

    @Override
    public List<AdjFactor> selectAll() {
        return adjFactorMapper.selectAll();
    }

    @Override
    public AdjFactor selectById(Long id) {
        return adjFactorMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return adjFactorMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(AdjFactor entity) {
        return adjFactorMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(AdjFactor entity) {
        return adjFactorMapper.insert(entity) > 0;
    }

}
