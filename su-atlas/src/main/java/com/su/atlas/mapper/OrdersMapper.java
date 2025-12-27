package com.su.atlas.mapper;

import com.su.atlas.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OrdersMapper {

    List<Orders> selectAll();

    Orders selectById(@Param("id") UUID id);

    int deleteById(@Param("id") UUID id);

    int updateById(Orders entity);

    int insert(Orders entity);

}
