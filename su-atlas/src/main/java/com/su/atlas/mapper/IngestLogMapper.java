package com.su.atlas.mapper;

import com.su.atlas.entity.IngestLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface IngestLogMapper {

    List<IngestLog> selectAll();

    IngestLog selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(IngestLog entity);

    int insert(IngestLog entity);

}
