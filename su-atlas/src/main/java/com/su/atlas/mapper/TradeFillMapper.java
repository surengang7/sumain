package com.su.atlas.mapper;

import com.su.atlas.entity.TradeFill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TradeFillMapper {

    List<TradeFill> selectAll();

    TradeFill selectById(@Param("id") UUID id);

    int deleteById(@Param("id") UUID id);

    int updateById(TradeFill entity);

    int insert(TradeFill entity);

}
