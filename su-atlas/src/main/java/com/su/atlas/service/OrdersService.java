package com.su.atlas.service;

import com.su.atlas.entity.Orders;
import java.util.List;

public interface OrdersService {

    List<Orders> selectAll();

    Orders selectById(UUID id);

    boolean deleteById(UUID id);

    boolean updateById(Orders entity);

    boolean insert(Orders entity);

}
