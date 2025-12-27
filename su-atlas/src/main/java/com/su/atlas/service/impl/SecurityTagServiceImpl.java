package com.su.atlas.service.impl;

import com.su.atlas.entity.SecurityTag;
import com.su.atlas.mapper.SecurityTagMapper;
import com.su.atlas.service.SecurityTagService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SecurityTagServiceImpl implements SecurityTagService {

    @Resource
    private SecurityTagMapper securityTagMapper;

    @Override
    public List<SecurityTag> selectAll() {
        return securityTagMapper.selectAll();
    }

    @Override
    public SecurityTag selectById(Long id) {
        return securityTagMapper.selectById(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return securityTagMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateById(SecurityTag entity) {
        return securityTagMapper.updateById(entity) > 0;
    }

    @Override
    public boolean insert(SecurityTag entity) {
        return securityTagMapper.insert(entity) > 0;
    }

}
