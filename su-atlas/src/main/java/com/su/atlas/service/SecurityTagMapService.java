package com.su.atlas.service;

import com.su.atlas.entity.SecurityTagMap;
import java.util.List;

public interface SecurityTagMapService {

    List<SecurityTagMap> selectAll();

    boolean insert(SecurityTagMap entity);

    // 未检测到唯一主键（可能是复合主键表），这里只生成 insert/selectAll。
}
