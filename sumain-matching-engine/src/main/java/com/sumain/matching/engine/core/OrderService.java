package com.sumain.matching.engine.core;


import com.sumain.matching.engine.model.BaseOrder;

import java.util.List;

public interface OrderService<O extends BaseOrder> {

    List<O> loadingOrders();

    void update(O o);

    void withdraw(O o);

    void withdrawAll();


}
