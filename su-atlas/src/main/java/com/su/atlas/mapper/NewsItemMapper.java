package com.su.atlas.mapper;

import com.su.atlas.entity.NewsItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface NewsItemMapper {

    List<NewsItem> selectAll();

    NewsItem selectById(@Param("id") UUID id);

    int deleteById(@Param("id") UUID id);

    int updateById(NewsItem entity);

    int insert(NewsItem entity);

}
