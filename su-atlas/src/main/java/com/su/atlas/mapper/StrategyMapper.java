package com.su.atlas.mapper;

import com.su.atlas.entity.Strategy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StrategyMapper {

    List<Strategy> selectAll();

    Strategy selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(Strategy entity);

    int insert(Strategy entity);

}
