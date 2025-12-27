package com.su.atlas.service;

import com.su.atlas.entity.IngestJob;
import java.util.List;

public interface IngestJobService {

    List<IngestJob> selectAll();

    IngestJob selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(IngestJob entity);

    boolean insert(IngestJob entity);

}
