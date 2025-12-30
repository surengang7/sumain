package com.su.atlas.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.su.atlas.entity.Exchange;
import com.su.atlas.enums.ExchangeEnum;
import com.su.atlas.mapper.ExchangeMapper;
import com.su.atlas.service.ExchangeService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Resource
    private ExchangeMapper baseMapper;


    @Override
    public void init() {
        List<Exchange> isExists = findAll();
        Set<String> codeSet = isExists.stream().map(Exchange::getExchangeCode).collect(Collectors.toSet());

        List<Exchange> exchangeList = new ArrayList<>();
        for (ExchangeEnum value : ExchangeEnum.values()) {
            if(codeSet.contains(value.getCode())) continue;
            Exchange exchange = new Exchange();
            exchange.setExchangeCode(value.getCode());
            exchange.setExchangeTimezone(value.getTimezone());
            exchange.setExchangeName(value.getName());
            exchangeList.add(exchange);
        }
        baseMapper.insert(exchangeList);
    }

    @Override
    public List<Exchange> findAll() {
        QueryWrapper<Exchange> queryWrapper = new QueryWrapper<>();
        return baseMapper.selectList(queryWrapper);
    }
}
