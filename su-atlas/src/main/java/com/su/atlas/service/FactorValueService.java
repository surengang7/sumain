package com.su.atlas.service;

import com.su.atlas.entity.FactorValue;
import java.util.List;

public interface FactorValueService {

    List<FactorValue> selectAll();

    FactorValue selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(FactorValue entity);

    boolean insert(FactorValue entity);

}
