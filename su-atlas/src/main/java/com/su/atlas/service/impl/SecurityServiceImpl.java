package com.su.atlas.service.impl;

import com.su.atlas.entity.Security;
import com.su.atlas.mapper.SecurityMapper;
import com.su.atlas.service.SecurityService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SecurityServiceImpl implements SecurityService {

    @Resource
    private SecurityMapper securityMapper;

    @Override
    public List<Security> selectAll() {
        return securityMapper.selectAll();
    }

    @Override
    public Security selectById(Long id) {
        return securityMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return securityMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(Security entity) {
        return securityMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(Security entity) {
        return securityMapper.insert(entity) > 0;
    }

}
