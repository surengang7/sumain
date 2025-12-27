package com.su.atlas.mapper;

import com.su.atlas.entity.StrategyRun;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StrategyRunMapper {

    List<StrategyRun> selectAll();

    StrategyRun selectById(@Param("id") UUID id);

    int deleteById(@Param("id") UUID id);

    int updateById(StrategyRun entity);

    int insert(StrategyRun entity);

}
