package com.su.atlas.service;

import com.su.atlas.entity.PositionTarget;
import java.util.List;

public interface PositionTargetService {

    List<PositionTarget> selectAll();

    boolean insert(PositionTarget entity);

    // 未检测到唯一主键（可能是复合主键表），这里只生成 insert/selectAll。
}
