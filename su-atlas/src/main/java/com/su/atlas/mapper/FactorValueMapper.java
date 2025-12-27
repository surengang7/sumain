package com.su.atlas.mapper;

import com.su.atlas.entity.FactorValue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FactorValueMapper {

    List<FactorValue> selectAll();

    FactorValue selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(FactorValue entity);

    int insert(FactorValue entity);

}
