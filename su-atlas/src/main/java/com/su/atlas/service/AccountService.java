package com.su.atlas.service;

import com.su.atlas.entity.Account;
import java.util.List;

public interface AccountService {

    List<Account> selectAll();

    Account selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(Account entity);

    boolean insert(Account entity);

}
