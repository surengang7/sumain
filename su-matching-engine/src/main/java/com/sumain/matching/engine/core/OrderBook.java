package com.sumain.matching.engine.core;

import com.sumain.matching.engine.model.BaseOrder;
import lombok.Getter;

import java.util.concurrent.PriorityBlockingQueue;

@Getter
public class OrderBook<O extends BaseOrder> {

    private final PriorityBlockingQueue<O> buyQueue =
            new PriorityBlockingQueue<>(100, OrderBookComparator.buyOrderComparator());

    private final PriorityBlockingQueue<O> sellQueue =
            new PriorityBlockingQueue<>(100, OrderBookComparator.sellOrderComparator());

}
