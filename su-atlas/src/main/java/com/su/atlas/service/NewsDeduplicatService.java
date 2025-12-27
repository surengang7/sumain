package com.su.atlas.service;

import com.su.atlas.entity.NewsDeduplicat;
import java.util.List;

public interface NewsDeduplicatService {

    List<NewsDeduplicat> selectAll();

    boolean insert(NewsDeduplicat entity);

    // 未检测到唯一主键（可能是复合主键表），这里只生成 insert/selectAll。
}
