package com.sumain.matching.engine.core;

import com.sumain.matching.engine.model.BaseOrder;
import com.sumain.matching.engine.model.BaseTrade;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class MatchingEngineIn<T extends BaseTrade,O extends BaseOrder> {


    /**
     * 做并发优化的时候，需要加中间层，采用Disruptor框架
     */

    @Resource
    private MatchingEngine<T,O> matchingEngine;



}
