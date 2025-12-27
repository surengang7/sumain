package com.su.atlas.mapper;

import com.su.atlas.entity.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AccountMapper {

    List<Account> selectAll();

    Account selectById(@Param("id") Long id);

    int deleteById(@Param("id") Long id);

    int updateById(Account entity);

    int insert(Account entity);

}
