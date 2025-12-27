package com.su.atlas.service.impl;

import com.su.atlas.entity.Account;
import com.su.atlas.mapper.AccountMapper;
import com.su.atlas.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountMapper accountMapper;

    @Override
    public List<Account> selectAll() {
        return accountMapper.selectAll();
    }

    @Override
    public Account selectById(Long id) {
        return accountMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return accountMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(Account entity) {
        return accountMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(Account entity) {
        return accountMapper.insert(entity) > 0;
    }

}
