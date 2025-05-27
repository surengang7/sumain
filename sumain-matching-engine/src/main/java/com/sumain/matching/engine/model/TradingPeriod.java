package com.sumain.matching.engine.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
public class TradingPeriod {

    /**
     * 早盘
     */
    private TimeDuration morningAuction;
    private TimeDuration morningUnCancelable;
    private TimeDuration morningMatching;
    private TimeDuration morningFree;

    /**
     * 午盘
     */
    private TimeDuration afternoonFree;
    private TimeDuration afternoonAuction;
    private TimeDuration afternoonUnCancelable;
    private TimeDuration afternoonMatching;


    /**
     * 此时间是否允许交易
     */
    public Boolean allowPutOrder(LocalDateTime now){
        LocalTime localTime = now.toLocalTime();
        return this.morningAuction.isWithin(localTime) ||
                this.morningUnCancelable.isWithin(localTime) ||
                this.morningFree.isWithin(localTime) ||
                this.afternoonFree.isWithin(localTime) ||
                this.afternoonAuction.isWithin(localTime) ||
                this.afternoonUnCancelable.isWithin(localTime);
    }

    /**
     * 此时间是否允许撤单
     */
    public Boolean allowWithdraw(LocalDateTime now){
        LocalTime localTime = now.toLocalTime();
        return this.morningAuction.isWithin(localTime) ||
                this.morningFree.isWithin(localTime) ||
                this.afternoonFree.isWithin(localTime) ||
                this.afternoonAuction.isWithin(localTime);
    }

    public Boolean isFreeTransactions(LocalDateTime now){
        LocalTime localTime = now.toLocalTime();
        return this.morningFree.isWithin(localTime) || this.afternoonFree.isWithin(localTime);
    }

    public Boolean isCallAuction(LocalDateTime now){
        LocalTime localTime = now.toLocalTime();
        return this.morningAuction.isWithin(localTime) ||
                this.morningUnCancelable.isWithin(localTime) ||
                this.afternoonAuction.isWithin(localTime) ||
                this.afternoonUnCancelable.isWithin(localTime);
    }


}
