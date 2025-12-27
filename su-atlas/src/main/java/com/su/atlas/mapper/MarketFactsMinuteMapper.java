package com.su.atlas.mapper;

import com.su.atlas.entity.MarketFactsMinute;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MarketFactsMinuteMapper {

    List<MarketFactsMinute> selectAll();

    MarketFactsMinute selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(MarketFactsMinute entity);

    int insert(MarketFactsMinute entity);

}
