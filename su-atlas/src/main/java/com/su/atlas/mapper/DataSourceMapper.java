package com.su.atlas.mapper;

import com.su.atlas.entity.DataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DataSourceMapper {

    List<DataSource> selectAll();

    DataSource selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(DataSource entity);

    int insert(DataSource entity);

}
