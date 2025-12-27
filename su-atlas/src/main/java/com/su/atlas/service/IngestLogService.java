package com.su.atlas.service;

import com.su.atlas.entity.IngestLog;
import java.util.List;

public interface IngestLogService {

    List<IngestLog> selectAll();

    IngestLog selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(IngestLog entity);

    boolean insert(IngestLog entity);

}
