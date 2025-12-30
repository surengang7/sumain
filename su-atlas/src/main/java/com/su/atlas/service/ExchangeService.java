package com.su.atlas.service;


import com.su.atlas.entity.Exchange;

import java.util.List;

public interface ExchangeService {


    void init();

    List<Exchange> findAll();
}
