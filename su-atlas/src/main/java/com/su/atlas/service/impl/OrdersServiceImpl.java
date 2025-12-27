package com.su.atlas.service.impl;

import com.su.atlas.mapper.OrdersMapper;
import com.su.atlas.service.OrdersService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Resource
    private OrdersMapper baseMapper;


}
