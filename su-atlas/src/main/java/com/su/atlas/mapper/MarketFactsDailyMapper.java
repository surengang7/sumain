package com.su.atlas.mapper;

import com.su.atlas.entity.MarketFactsDaily;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface MarketFactsDailyMapper {

    List<MarketFactsDaily> selectAll();

    MarketFactsDaily selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(MarketFactsDaily entity);

    int insert(MarketFactsDaily entity);

}
