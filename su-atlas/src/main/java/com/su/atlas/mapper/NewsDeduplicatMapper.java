package com.su.atlas.mapper;

import com.su.atlas.entity.NewsDeduplicat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface NewsDeduplicatMapper {

    List<NewsDeduplicat> selectAll();

    int insert(NewsDeduplicat entity);

    // 未检测到唯一主键（可能是复合主键表），这里只生成 insert/selectAll。
    // 你可以自行补充按条件查询/删除/更新方法。
}
