package com.sumain.matching.engine.core;

import com.sumain.matching.engine.model.BaseOrder;

import java.util.Comparator;

public class OrderBookComparator {

    /**
     * 买单优先级比较器：价格高优先，价格相同时间早优先
     */
    public static Comparator<BaseOrder> buyOrderComparator() {
        return (o1, o2) -> {
            int priceCompare = o2.getPrice().compareTo(o1.getPrice());
            if (priceCompare != 0) {
                return priceCompare;
            }
            return o1.getOrderTime().compareTo(o2.getOrderTime());
        };
    }

    /**
     * 卖单优先级比较器：价格低优先，价格相同时间早优先
     */
    public static Comparator<BaseOrder> sellOrderComparator() {
        return Comparator.comparing(BaseOrder::getPrice).thenComparing(BaseOrder::getOrderTime);
    }

}
