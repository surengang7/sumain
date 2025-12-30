package com.su.atlas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.su.atlas.entity.Security;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface SecurityMapper extends BaseMapper<Security> {

    int batchUpsertByMarketCode(@Param("list") List<Security> list);

}
