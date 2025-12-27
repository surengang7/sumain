package com.su.atlas.service.impl;

import com.su.atlas.entity.Exchange;
import com.su.atlas.mapper.ExchangeMapper;
import com.su.atlas.service.ExchangeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Resource
    private ExchangeMapper exchangeMapper;

    @Override
    public List<Exchange> selectAll() {
        return exchangeMapper.selectAll();
    }

    @Override
    public Exchange selectById(Long id) {
        return exchangeMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return exchangeMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(Exchange entity) {
        return exchangeMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(Exchange entity) {
        return exchangeMapper.insert(entity) > 0;
    }

}
