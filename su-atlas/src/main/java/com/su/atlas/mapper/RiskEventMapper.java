package com.su.atlas.mapper;

import com.su.atlas.entity.RiskEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RiskEventMapper {

    List<RiskEvent> selectAll();

    RiskEvent selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(RiskEvent entity);

    int insert(RiskEvent entity);

}
