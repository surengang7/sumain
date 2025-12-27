package com.su.atlas.mapper;

import com.su.atlas.entity.IngestJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface IngestJobMapper {

    List<IngestJob> selectAll();

    IngestJob selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(IngestJob entity);

    int insert(IngestJob entity);

}
