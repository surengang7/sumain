package com.su.atlas.mapper;

import com.su.atlas.entity.CashSnapshot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface CashSnapshotMapper {

    List<CashSnapshot> selectAll();

    CashSnapshot selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(CashSnapshot entity);

    int insert(CashSnapshot entity);

}
