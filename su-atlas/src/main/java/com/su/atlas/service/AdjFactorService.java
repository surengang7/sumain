package com.su.atlas.service;

import com.su.atlas.entity.AdjFactor;
import java.util.List;

public interface AdjFactorService {

    List<AdjFactor> selectAll();

    AdjFactor selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(AdjFactor entity);

    boolean insert(AdjFactor entity);

}
