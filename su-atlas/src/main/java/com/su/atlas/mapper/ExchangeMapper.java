package com.su.atlas.mapper;

import com.su.atlas.entity.Exchange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ExchangeMapper {

    List<Exchange> selectAll();

    Exchange selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(Exchange entity);

    int insert(Exchange entity);

}
