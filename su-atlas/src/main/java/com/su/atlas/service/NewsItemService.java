package com.su.atlas.service;

import com.su.atlas.entity.NewsItem;
import java.util.List;

public interface NewsItemService {

    List<NewsItem> selectAll();

    NewsItem selectById(UUID id);

    boolean deleteById(UUID id);

    boolean updateById(NewsItem entity);

    boolean insert(NewsItem entity);

}
