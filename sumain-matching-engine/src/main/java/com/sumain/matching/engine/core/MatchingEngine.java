package com.sumain.matching.engine.core;



import com.sumain.matching.engine.model.BaseOrder;
import com.sumain.matching.engine.model.BaseTrade;
import com.sumain.matching.engine.model.MarketDepthLevel;
import com.sumain.matching.engine.model.OrderDirection;

import java.util.List;

public interface MatchingEngine<T extends BaseTrade,O extends BaseOrder> {

    /**
     * put order: Based on BaseOrder implementation class
     */
    void put(O order);


    /**
     * withdraw order
     */
    void withdraw(O order);

    /**
     * get market information
     */
    List<MarketDepthLevel> getMarketInfo(String productCode);

    void callAuctionMatch();

    void withdrawAll();

    Boolean check(String productCode, String userNo, OrderDirection side);
}
