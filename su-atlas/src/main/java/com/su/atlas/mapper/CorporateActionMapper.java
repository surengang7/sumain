package com.su.atlas.mapper;

import com.su.atlas.entity.CorporateAction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CorporateActionMapper {

    List<CorporateAction> selectAll();

    CorporateAction selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(CorporateAction entity);

    int insert(CorporateAction entity);

}
