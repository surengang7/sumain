package com.su.atlas.mapper;

import com.su.atlas.entity.Security;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SecurityMapper {

    List<Security> selectAll();

    Security selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(Security entity);

    int insert(Security entity);

}
