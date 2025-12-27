package com.su.atlas.service;

import com.su.atlas.entity.CashSnapshot;
import java.util.List;

public interface CashSnapshotService {

    List<CashSnapshot> selectAll();

    CashSnapshot selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(CashSnapshot entity);

    boolean insert(CashSnapshot entity);

}
