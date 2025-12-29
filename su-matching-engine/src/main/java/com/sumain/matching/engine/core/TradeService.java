package com.sumain.matching.engine.core;

import com.sumain.matching.engine.model.BaseOrder;
import com.sumain.matching.engine.model.BaseTrade;
import com.sumain.matching.engine.model.OrderDirection;

import java.math.BigDecimal;

public interface TradeService<T extends BaseTrade,O extends BaseOrder> {

    T create(O buyOrder, O sellOrder, OrderDirection side, BigDecimal price);


}
