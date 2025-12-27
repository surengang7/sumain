package com.su.atlas.service;

import com.su.atlas.entity.NewsSecurityMap;
import java.util.List;

public interface NewsSecurityMapService {

    List<NewsSecurityMap> selectAll();

    boolean insert(NewsSecurityMap entity);

    // 未检测到唯一主键（可能是复合主键表），这里只生成 insert/selectAll。
}
