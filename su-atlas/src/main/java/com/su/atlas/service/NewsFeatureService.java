package com.su.atlas.service;

import com.su.atlas.entity.NewsFeature;
import java.util.List;

public interface NewsFeatureService {

    List<NewsFeature> selectAll();

    NewsFeature selectById(UUID id);

    boolean deleteById(UUID id);

    boolean updateById(NewsFeature entity);

    boolean insert(NewsFeature entity);

}
