package com.su.atlas.service.impl;

import com.su.atlas.entity.TradeFill;
import com.su.atlas.mapper.TradeFillMapper;
import com.su.atlas.service.TradeFillService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TradeFillServiceImpl implements TradeFillService {

    @Resource
    private TradeFillMapper tradeFillMapper;

    @Override
    public List<TradeFill> selectAll() {
        return tradeFillMapper.selectAll();
    }

    @Override
    public TradeFill selectById(UUID id) {
        return tradeFillMapper.selectById(id);
    }

    @Override
    public boolean deleteById(UUID id) {
        return tradeFillMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(TradeFill entity) {
        return tradeFillMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(TradeFill entity) {
        return tradeFillMapper.insert(entity) > 0;
    }

}
