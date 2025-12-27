package com.su.atlas.service.impl;

import com.su.atlas.entity.Orders;
import com.su.atlas.mapper.OrdersMapper;
import com.su.atlas.service.OrdersService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Resource
    private OrdersMapper ordersMapper;

    @Override
    public List<Orders> selectAll() {
        return ordersMapper.selectAll();
    }

    @Override
    public Orders selectById(UUID id) {
        return ordersMapper.selectById(id);
    }

    @Override
    public boolean deleteById(UUID id) {
        return ordersMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(Orders entity) {
        return ordersMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(Orders entity) {
        return ordersMapper.insert(entity) > 0;
    }

}
