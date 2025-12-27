package com.su.atlas.service;

import com.su.atlas.entity.DataSource;
import java.util.List;

public interface DataSourceService {

    List<DataSource> selectAll();

    DataSource selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(DataSource entity);

    boolean insert(DataSource entity);

}
