package com.su.atlas.service.impl;

import com.su.atlas.entity.SecurityTagMap;
import com.su.atlas.mapper.SecurityTagMapMapper;
import com.su.atlas.service.SecurityTagMapService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SecurityTagMapServiceImpl implements SecurityTagMapService {

    @Resource
    private SecurityTagMapMapper securityTagMapMapper;

    @Override
    public List<SecurityTagMap> selectAll() {
        return securityTagMapMapper.selectAll();
    }

    @Override
    public boolean insert(SecurityTagMap entity) {
        return securityTagMapMapper.insert(entity) > 0;
    }

}
