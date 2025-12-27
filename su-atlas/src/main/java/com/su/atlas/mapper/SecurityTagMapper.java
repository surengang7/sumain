package com.su.atlas.mapper;

import com.su.atlas.entity.SecurityTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface SecurityTagMapper {

    List<SecurityTag> selectAll();

    SecurityTag selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(SecurityTag entity);

    int insert(SecurityTag entity);

}
