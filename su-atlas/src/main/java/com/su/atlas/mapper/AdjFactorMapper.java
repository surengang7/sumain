package com.su.atlas.mapper;

import com.su.atlas.entity.AdjFactor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AdjFactorMapper {

    List<AdjFactor> selectAll();

    AdjFactor selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(AdjFactor entity);

    int insert(AdjFactor entity);

}
