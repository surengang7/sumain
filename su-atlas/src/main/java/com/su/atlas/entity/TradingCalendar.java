package com.su.atlas.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 交易日历（用于回测、对齐数据、节假日判断）
 * 主键：(market, trade_date)
 */
@Data
@Accessors(chain = true)
@TableName("trading_calendar")
public class TradingCalendar {

    /** 市场：CN_A/HK/US 等 */
    @TableField("market")
    private String market;

    /** 自然日 */
    @TableField("trade_date")
    private LocalDate tradeDate;

    /** 是否交易日 */
    @TableField("is_trading_day")
    private Boolean isTradingDay;

    /** 开盘时间（可空） */
    @TableField("open_time")
    private LocalTime openTime;

    /** 收盘时间（可空） */
    @TableField("close_time")
    private LocalTime closeTime;

    /** 创建时间 */
    @TableField("created_time")
    private LocalDateTime createdTime;
}

