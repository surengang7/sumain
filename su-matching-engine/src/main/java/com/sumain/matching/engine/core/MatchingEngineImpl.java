package com.sumain.matching.engine.core;


import com.sumain.matching.engine.config.TimeConfig;
import com.sumain.matching.engine.model.BaseOrder;
import com.sumain.matching.engine.model.BaseTrade;
import com.sumain.matching.engine.model.MarketDepthLevel;
import com.sumain.matching.engine.model.OrderDirection;
import com.sumain.matching.engine.model.TradingPeriod;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.PriorityBlockingQueue;


@Service
@Slf4j
public class MatchingEngineImpl<T extends BaseTrade,O extends BaseOrder> implements MatchingEngine<T,O> {

    @Resource
    private OrderBookManager<O> orderBookManager;

    @Resource
    private TimeConfig timeConfig;

    @Resource
    private MatchingEngineOut<T,O> matchingEngineOut;


    @Override
    public void put(O order) {
        if (order.getSide() == OrderDirection.BUY) {
            freeBuyOrderHandler(order);
        } else {
            freeSellOrderHandler(order);
        }
        matchingEngineOut.toClient(order.getProductCode());
    }

    @Override
    public void withdraw(O order) {
        OrderBook<O> orderBook = orderBookManager.getOrderBook(order.getProductCode());
        synchronized (orderBook){
            PriorityBlockingQueue<O> orderQueue = order.getSide() == OrderDirection.BUY ? orderBook.getBuyQueue() : orderBook.getSellQueue();

            // 找出订单并移除
            Iterator<O> iterator = orderQueue.iterator();
            while (iterator.hasNext()) {
                O existingOrder = iterator.next();
                if (existingOrder.getOrderNo().equals(order.getOrderNo())) {
                    iterator.remove(); // 安全移除
                    break;
                }
            }
            matchingEngineOut.withdraw(order);
        }
        matchingEngineOut.toClient(order.getProductCode());
    }

    @Override
    public List<MarketDepthLevel> getMarketInfo(String productCode) {
        OrderBook<O> orderBook = orderBookManager.getOrderBook(productCode);

        Map<BigDecimal, Long> buyMap = new TreeMap<>(Comparator.reverseOrder()); // 买单价格降序
        Map<BigDecimal, Long> sellMap = new TreeMap<>();                         // 卖单价格升序

        synchronized (orderBook) {
            for (O order : orderBook.getBuyQueue()) {
                buyMap.merge(order.getPrice(), order.getRemainingQuantity(), Long::sum);
            }

            for (O order : orderBook.getSellQueue()) {
                sellMap.merge(order.getPrice(), order.getRemainingQuantity(), Long::sum);
            }
        }

        List<MarketDepthLevel> result = new ArrayList<>();

        int level = 1;
        for (Map.Entry<BigDecimal, Long> entry : buyMap.entrySet()) {
            if (level > 5) break;
            result.add(new MarketDepthLevel(level, entry.getKey(), entry.getValue(), OrderDirection.BUY));
            level++;
        }

        level = 1;
        for (Map.Entry<BigDecimal, Long> entry : sellMap.entrySet()) {
            if (level > 5) break;
            result.add(new MarketDepthLevel(level, entry.getKey(), entry.getValue(), OrderDirection.SELL));
            level++;
        }

        return result;
    }

    @Override
    public void withdrawAll() {
        List<OrderBook<O>> orderBooks = orderBookManager.getOrderBooks();
        for (OrderBook<O> orderBook : orderBooks) {
            orderBook.getBuyQueue().clear();
            orderBook.getSellQueue().clear();
        }
        matchingEngineOut.withdrawAll();
    }

    @Override
    public Boolean check(String productCode, String userNo, OrderDirection side) {
        OrderBook<O> orderBook = orderBookManager.getOrderBook(productCode);
        synchronized (orderBook) {
            PriorityBlockingQueue<O> orderQueue;
            if (side == OrderDirection.BUY) {
                orderQueue = orderBook.getSellQueue();
            } else {
                orderQueue = orderBook.getBuyQueue();
            }
            for (O order : orderQueue) {
                if (userNo.equals(order.getUserNo())) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public void callAuctionMatch() {
        Set<String> productCodes = orderBookManager.getProductCodes();
        for (String productCode : productCodes) {
            OrderBook<O> orderBook = orderBookManager.getOrderBook(productCode);
            PriorityBlockingQueue<BaseOrder> buyQueue = new PriorityBlockingQueue<>(orderBook.getBuyQueue());
            PriorityBlockingQueue<BaseOrder> sellQueue = new PriorityBlockingQueue<>(orderBook.getSellQueue());

            // 获取买一价和卖一价
            BaseOrder buyOne;
            BaseOrder sellOne;
            if ((buyOne = buyQueue.peek()) == null || (sellOne = sellQueue.peek()) == null) {
                continue; // 没法成交
            }
            BigDecimal buyPrice = buyOne.getPrice();
            BigDecimal sellPrice = sellOne.getPrice();
            // 无交集，无法成交（集合竞价要求买一 >= 卖一）
            if (buyPrice.compareTo(sellPrice) < 0) {
                continue;
            }

            // 步骤1：生成候选价格集合（从买一到卖一之间的所有价格） todo 需要优化价格
            Set<BigDecimal> priceSet = new TreeSet<>(Comparator.reverseOrder());
            for (BaseOrder buy : buyQueue) {
                priceSet.add(buy.getPrice());
            }
            for (BaseOrder sell : sellQueue) {
                priceSet.add(sell.getPrice());
            }

            BigDecimal matchPrice = null;
            Long maxMatchedQuantity = 0L;

            // 步骤2：找出“撮合价”（成交通量最大）
            for (BigDecimal candidatePrice : priceSet) {
                Long buyQuantity = buyQueue.stream()
                        .filter(order -> order.getPrice().compareTo(candidatePrice) >= 0)
                        .map(BaseOrder::getRemainingQuantity)
                        .reduce(0L, Long::sum);

                Long sellQuantity = sellQueue.stream()
                        .filter(order -> order.getPrice().compareTo(candidatePrice) <= 0)
                        .map(BaseOrder::getRemainingQuantity)
                        .reduce(0L, Long::sum);

                Long matchedQuantity = Math.min(buyQuantity,sellQuantity);
                if (matchedQuantity.compareTo(maxMatchedQuantity) > 0) {
                    maxMatchedQuantity = matchedQuantity;
                    matchPrice = candidatePrice;
                }
            }

            if (matchPrice == null) {
                continue; // 没找到撮合价
            }
            log.info("成交价为:{}",matchPrice);
            log.info("成交数量为:{}",maxMatchedQuantity);

            // 步骤3：按撮合价撮合订单
            long remainingBuy = maxMatchedQuantity;
            while (remainingBuy > 0 && !orderBook.getBuyQueue().isEmpty() && !orderBook.getSellQueue().isEmpty()) {
                O buyOrder = orderBook.getBuyQueue().peek();
                O sellOrder = orderBook.getSellQueue().peek();

                T t = matchingEngineOut.createTrade(buyOrder, sellOrder, null, matchPrice);

                remainingBuy = remainingBuy - t.getQuantity();

                // 更新成交量、成交均价、状态
                buyOrder.update(t.getQuantity(),t.getPrice());
                sellOrder.update(t.getQuantity(),t.getPrice());
                buyOrder.updateStatus();
                sellOrder.updateStatus();
                matchingEngineOut.updateOrder(buyOrder);
                matchingEngineOut.updateOrder(sellOrder);

                // 移除买单
                if(buyOrder.getRemainingQuantity() == 0){
                    @SuppressWarnings("unused")
                    boolean remove = orderBook.getBuyQueue().remove(buyOrder);
                }

                // 移除卖单
                if(sellOrder.getRemainingQuantity() == 0){
                    @SuppressWarnings("unused")
                    boolean remove = orderBook.getSellQueue().remove(sellOrder);
                }

            }
            matchingEngineOut.toClient(productCode);
        }
    }



    //处理买单
    private void freeBuyOrderHandler(O buyOrder) {
        OrderBook<O> orderBook = orderBookManager.getOrderBook(buyOrder.getProductCode());

        synchronized (orderBook) {
            PriorityBlockingQueue<O> buyQueue = orderBook.getBuyQueue();
            PriorityBlockingQueue<O> sellQueue = orderBook.getSellQueue();
            TradingPeriod tradingPeriod = timeConfig.getTradingTimeConfig();

            while (true) {
                // 为集合竞价时段则不进行撮合
                if (tradingPeriod.isCallAuction(buyOrder.getOrderTime())) {
                    break;
                }

                O sellOrder = sellQueue.peek();
                if (sellOrder == null) {
                    break;
                }

                // 检查是否能成交
                if (buyOrder.getPrice().compareTo(sellOrder.getPrice()) < 0) {
                    break;
                }

                // 取出卖单（再次确保是同一个）
                sellOrder = sellQueue.poll();
                if (sellOrder == null) {
                    break;
                }

                // 生成交易记录
                T t = matchingEngineOut.createTrade(buyOrder, sellOrder, OrderDirection.BUY, null);

                // 更新买单和卖单状态
                buyOrder.update(t.getQuantity(), t.getPrice());
                sellOrder.update(t.getQuantity(), t.getPrice());
                sellOrder.updateStatus();
                matchingEngineOut.updateOrder(sellOrder);

                // 卖单还有剩余，重新入队
                if (sellOrder.getRemainingQuantity() > 0) {
                    sellQueue.put(sellOrder);
                }

                // 买单已撮合完成，退出
                if (buyOrder.getRemainingQuantity() == 0) {
                    break;
                }
            }
            if(!Objects.equals(buyOrder.getRemainingQuantity(), buyOrder.getQuantity())){
                buyOrder.updateStatus();
                matchingEngineOut.updateOrder(buyOrder);
            }
            // 撮合未完成部分放回买队列
            if(buyOrder.getRemainingQuantity() > 0){
                buyQueue.put(buyOrder);
            }

        }
    }



    // 处理卖单
    private void freeSellOrderHandler(O sellOrder) {
        OrderBook<O> orderBook = orderBookManager.getOrderBook(sellOrder.getProductCode());
        synchronized (orderBook) {
            PriorityBlockingQueue<O> buyQueue = orderBookManager.getOrderBook(sellOrder.getProductCode()).getBuyQueue();
            PriorityBlockingQueue<O> sellQueue = orderBookManager.getOrderBook(sellOrder.getProductCode()).getSellQueue();
            TradingPeriod tradingPeriod = timeConfig.getTradingTimeConfig();
            while (true) {

                if (tradingPeriod.isCallAuction(sellOrder.getOrderTime())) {
                    break;
                }
                O buyOrder = buyQueue.peek();   //获取买方首位元素（最高价）
                if (buyOrder == null) {
                    break;
                }
                // 检查能否撮合成交
                if (buyOrder.getPrice().compareTo(sellOrder.getPrice()) < 0) {
                    break;
                }

                buyOrder = buyQueue.poll();     //取出并删除首位元素
                if (buyOrder == null || buyOrder.getPrice().compareTo(sellOrder.getPrice()) < 0) {
                    break;
                }

                // 生成交易
                T t = matchingEngineOut.createTrade(buyOrder, sellOrder, OrderDirection.SELL, null);

                // 更新成交量以及成交均价
                buyOrder.update(t.getQuantity(), t.getPrice());
                sellOrder.update(t.getQuantity(), t.getPrice());
                buyOrder.updateStatus();
                matchingEngineOut.updateOrder(buyOrder);

                // 如果买单还有剩余，重新放回队列
                if (buyOrder.getRemainingQuantity() > 0) {
                    buyQueue.put(buyOrder);
                }

                // 如果卖单已完成
                if (sellOrder.getRemainingQuantity() == 0) {
                    break;
                }
            }
            if(!Objects.equals(sellOrder.getRemainingQuantity(), sellOrder.getQuantity())){
                sellOrder.updateStatus();
                matchingEngineOut.updateOrder(sellOrder);
            }
            // 剩余未撮合部分加入卖盘队列
            if(sellOrder.getRemainingQuantity() > 0){
                sellQueue.put(sellOrder);
            }

        }

    }



}
