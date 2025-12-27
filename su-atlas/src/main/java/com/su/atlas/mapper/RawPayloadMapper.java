package com.su.atlas.mapper;

import com.su.atlas.entity.RawPayload;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RawPayloadMapper {

    List<RawPayload> selectAll();

    RawPayload selectById(@Param("id") UUID id);

    int deleteById(@Param("id") UUID id);

    int updateById(RawPayload entity);

    int insert(RawPayload entity);

}
