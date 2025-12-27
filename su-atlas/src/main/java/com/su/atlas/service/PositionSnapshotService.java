package com.su.atlas.service;

import com.su.atlas.entity.PositionSnapshot;
import java.util.List;

public interface PositionSnapshotService {

    List<PositionSnapshot> selectAll();

    boolean insert(PositionSnapshot entity);

    // 未检测到唯一主键（可能是复合主键表），这里只生成 insert/selectAll。
}
