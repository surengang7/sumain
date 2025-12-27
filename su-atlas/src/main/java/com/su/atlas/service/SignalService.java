package com.su.atlas.service;

import com.su.atlas.entity.Signal;
import java.util.List;

public interface SignalService {

    List<Signal> selectAll();

    Signal selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(Signal entity);

    boolean insert(Signal entity);

}
