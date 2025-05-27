package com.sumain.matching.engine.config;

import com.sumain.matching.engine.model.TimeDuration;
import com.sumain.common.utils.DateUtils;
import com.sumain.matching.engine.model.TradingPeriod;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class TimeConfig {

    /**
     * 二级市场配置
     */

    @Value("${sumain.matching.engine.tradingTime.morningAuction.start:09:00:00}")
    public String morningAuctionStart;

    @Value("${sumain.matching.engine.tradingTime.morningAuction.end:09:15:00}")
    public String morningAuctionEnd;

    @Value("${sumain.matching.engine.tradingTime.morningUnCancelable.start:09:15:00}")
    public String morningUnCancelableStart;

    @Value("${sumain.matching.engine.tradingTime.morningUnCancelable.end:09:20:00}")
    public String morningUnCancelableEnd;

    @Value("${sumain.matching.engine.tradingTime.morningMatching.start:09:20:00}")
    public String morningMatchingStart;

    @Value("${sumain.matching.engine.tradingTime.morningMatching.end:09:30:00}")
    public String morningMatchingEnd;

    @Value("${sumain.matching.engine.tradingTime.morningFree.start:09:30:00}")
    public String morningFreeStart;

    @Value("${sumain.matching.engine.tradingTime.morningFree.end:11:30:00}")
    public String morningFreeEnd;

    @Value("${sumain.matching.engine.tradingTime.afternoonFree.start:13:00:00}")
    public String afternoonFreeStart;

    @Value("${sumain.matching.engine.tradingTime.afternoonFree.end:16:00:00}")
    public String afternoonFreeEnd;

    @Value("${sumain.matching.engine.tradingTime.afternoonAuction.start:16:00:00}")
    public String afternoonAuctionStart;

    @Value("${sumain.matching.engine.tradingTime.afternoonAuction.end:16:15:00}")
    public String afternoonAuctionEnd;

    @Value("${sumain.matching.engine.tradingTime.afternoonUnCancelable.start:16:15:00}")
    public String afternoonUnCancelableStart;

    @Value("${sumain.matching.engine.tradingTime.afternoonUnCancelable.end:16:20:00}")
    public String afternoonUnCancelableEnd;

    @Value("${sumain.matching.engine.tradingTime.afternoonMatching.start:16:20:00}")
    public String afternoonMatchingStart;

    @Value("${sumain.matching.engine.tradingTime.afternoonMatching.end:16:30:00}")
    public String afternoonMatchingEnd;


    public TradingPeriod getTradingTimeConfig() {
        TradingPeriod tradingTimeConfig = new TradingPeriod();
        // 早盘
        tradingTimeConfig.setMorningAuction(TimeDuration.builder()
                .startTime(DateUtils.toLocalTime(this.getMorningAuctionStart()))
                .endTime(DateUtils.toLocalTime(this.getMorningAuctionEnd())).build());

        tradingTimeConfig.setMorningUnCancelable(TimeDuration.builder()
                .startTime(DateUtils.toLocalTime(this.getMorningUnCancelableStart()))
                .endTime(DateUtils.toLocalTime(this.getMorningUnCancelableEnd())).build());

        tradingTimeConfig.setMorningMatching(TimeDuration.builder()
                .startTime(DateUtils.toLocalTime(this.getMorningMatchingStart()))
                .endTime(DateUtils.toLocalTime(this.getMorningMatchingEnd())).build());

        tradingTimeConfig.setMorningFree(TimeDuration.builder()
                .startTime(DateUtils.toLocalTime(this.getMorningFreeStart()))
                .endTime(DateUtils.toLocalTime(this.getMorningFreeEnd())).build());

        // 午盘
        tradingTimeConfig.setAfternoonFree(TimeDuration.builder()
                .startTime(DateUtils.toLocalTime(this.getAfternoonFreeStart()))
                .endTime(DateUtils.toLocalTime(this.getAfternoonFreeEnd())).build());

        tradingTimeConfig.setAfternoonAuction(TimeDuration.builder()
                .startTime(DateUtils.toLocalTime(this.getAfternoonAuctionStart()))
                .endTime(DateUtils.toLocalTime(this.getAfternoonAuctionEnd())).build());

        tradingTimeConfig.setAfternoonUnCancelable(TimeDuration.builder()
                .startTime(DateUtils.toLocalTime(this.getAfternoonUnCancelableStart()))
                .endTime(DateUtils.toLocalTime(this.getAfternoonUnCancelableEnd())).build());

        tradingTimeConfig.setAfternoonMatching(TimeDuration.builder()
                .startTime(DateUtils.toLocalTime(this.getAfternoonMatchingStart()))
                .endTime(DateUtils.toLocalTime(this.getAfternoonMatchingEnd())).build());

        return tradingTimeConfig;
    }
}
