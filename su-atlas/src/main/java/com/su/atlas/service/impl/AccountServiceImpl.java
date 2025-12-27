package com.su.atlas.service.impl;

import com.su.atlas.mapper.AccountMapper;
import com.su.atlas.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountMapper baseMapper;



}
