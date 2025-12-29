package com.sumain.matching.engine.core;


import com.sumain.matching.engine.model.BaseOrder;
import com.sumain.matching.engine.model.OrderDirection;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class OrderBookManager<O extends BaseOrder> {

    private final Map<String, OrderBook<O>> productOrderBooks = new ConcurrentHashMap<>();

    @Resource
    private OrderService<O> orderService;

    public OrderBook<O> getOrderBook(String productId) {
        return productOrderBooks.computeIfAbsent(productId, id -> new OrderBook<>());
    }

    public List<OrderBook<O>> getOrderBooks(){
        return productOrderBooks.values().stream().toList();
    }

    public Set<String> getProductCodes(){
        return productOrderBooks.keySet();
    }

    @PostConstruct
    public void loadOrderBooks(){
        List<O> pendingMatchOrders = orderService.loadingOrders();

        for (O order : pendingMatchOrders) {
            log.info("loading order: {}",order);
            // 根据产品code获取对应的 OrderBook
            OrderBook<O> orderBook = getOrderBook(order.getProductCode());

            // 根据买卖方向将订单放入买单或卖单队列
            if (order.getSide() == OrderDirection.BUY) {
                orderBook.getBuyQueue().add(order);
            } else if (order.getSide() == OrderDirection.SELL) {
                orderBook.getSellQueue().add(order);
            }
        }
    }
}
