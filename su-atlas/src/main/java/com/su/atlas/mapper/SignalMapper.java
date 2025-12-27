package com.su.atlas.mapper;

import com.su.atlas.entity.Signal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SignalMapper {

    List<Signal> selectAll();

    Signal selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(Signal entity);

    int insert(Signal entity);

}
