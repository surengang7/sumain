package com.su.atlas.service;

import com.su.atlas.entity.Exchange;
import java.util.List;

public interface ExchangeService {

    List<Exchange> selectAll();

    Exchange selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(Exchange entity);

    boolean insert(Exchange entity);

}
