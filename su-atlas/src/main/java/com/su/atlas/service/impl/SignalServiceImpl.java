package com.su.atlas.service.impl;

import com.su.atlas.entity.Signal;
import com.su.atlas.mapper.SignalMapper;
import com.su.atlas.service.SignalService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SignalServiceImpl implements SignalService {

    @Resource
    private SignalMapper signalMapper;

    @Override
    public List<Signal> selectAll() {
        return signalMapper.selectAll();
    }

    @Override
    public Signal selectById(Long id) {
        return signalMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return signalMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(Signal entity) {
        return signalMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(Signal entity) {
        return signalMapper.insert(entity) > 0;
    }

}
