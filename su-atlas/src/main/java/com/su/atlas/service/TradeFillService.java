package com.su.atlas.service;

import com.su.atlas.entity.TradeFill;
import java.util.List;

public interface TradeFillService {

    List<TradeFill> selectAll();

    TradeFill selectById(UUID id);

    boolean deleteById(UUID id);

    boolean updateById(TradeFill entity);

    boolean insert(TradeFill entity);

}
