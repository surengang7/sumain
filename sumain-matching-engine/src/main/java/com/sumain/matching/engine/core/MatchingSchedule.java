package com.sumain.matching.engine.core;



import com.sumain.matching.engine.model.BaseOrder;
import com.sumain.matching.engine.model.BaseTrade;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class MatchingSchedule<T extends BaseTrade,O extends BaseOrder> {

    @Resource
    private MatchingEngine<T,O> matchingEngine;

    @Scheduled(cron = "${hqshuke.exchange.trade.secondaryMarket.job.morningCallAuctionMatch:0 30 9 * * *}")
    public void morningCallAuctionMatch(){
        log.info("开盘集合竞价匹配开始");
        matchingEngine.callAuctionMatch();
        log.info("开盘集合竞价匹配结束");
    }

    @Scheduled(cron = "${hqshuke.exchange.trade.secondaryMarket.job.afternoonCallAuctionMatch:0 0 19 * * *}")
    public void afternoonCallAuctionMatch(){
        log.info("收盘集合竞价匹配开始");
        matchingEngine.callAuctionMatch();
        log.info("收盘集合竞价匹配结束");
        log.info("收盘取消所有挂单");
        matchingEngine.withdrawAll();
        log.info("收盘取消所有挂单");
    }

}
