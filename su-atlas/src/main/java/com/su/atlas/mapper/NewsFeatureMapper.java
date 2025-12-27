package com.su.atlas.mapper;

import com.su.atlas.entity.NewsFeature;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface NewsFeatureMapper {

    List<NewsFeature> selectAll();

    NewsFeature selectById(@Param("id") UUID id);

    int deleteById(@Param("id") UUID id);

    int updateById(NewsFeature entity);

    int insert(NewsFeature entity);

}
