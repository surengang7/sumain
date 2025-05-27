package com.sumain.matching.engine.core;


import com.sumain.matching.engine.model.BaseOrder;
import com.sumain.matching.engine.model.BaseTrade;
import com.sumain.matching.engine.model.OrderDirection;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class MatchingEngineOut<T extends BaseTrade,O extends BaseOrder> {

    @Resource
    private TradeService<T,O> tradeService;

    @Resource
    private OrderService<O> orderService;

    @Resource
    private MarketDateService marketDateService;


    public void updateOrder(O order){
        orderService.update(order);
    }

    public T createTrade(O buyOrder, O sellOrder, OrderDirection side, BigDecimal price){
        return tradeService.create(buyOrder, sellOrder, side, price);
    }

    public void withdraw(O order){
        orderService.withdraw(order);
    }

    public void withdrawAll(){
        orderService.withdrawAll();
    }

    @Async("SseExecutor")
    public void toClient(String productCode){
        marketDateService.toClient(productCode);
    }

}
