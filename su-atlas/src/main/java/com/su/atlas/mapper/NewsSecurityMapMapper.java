package com.su.atlas.mapper;

import com.su.atlas.entity.NewsSecurityMap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface NewsSecurityMapMapper {

    List<NewsSecurityMap> selectAll();

    int insert(NewsSecurityMap entity);

    // 未检测到唯一主键（可能是复合主键表），这里只生成 insert/selectAll。
    // 你可以自行补充按条件查询/删除/更新方法。
}
