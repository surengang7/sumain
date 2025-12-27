package com.su.atlas.service.impl;

import com.su.atlas.mapper.SignalMapper;
import com.su.atlas.service.SignalService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class SignalServiceImpl implements SignalService {

    @Resource
    private SignalMapper signalMapper;


}
