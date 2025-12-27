package com.su.atlas.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 日线行情（OHLCV）
 * 主键：(security_id, trade_date)
 */
@Data
@Accessors(chain = true)
@TableName("market_facts_daily")
public class MarketFactsDaily implements Serializable {

    @Serial
    private static final long serialVersionUID = 655128339613460108L;

    /** 证券ID */
    @TableField("security_id")
    private Long securityId;

    /** 交易日 */
    @TableField("trade_date")
    private LocalDate tradeDate;

    /** 开盘价 */
    @TableField("open")
    private BigDecimal open;

    /** 最高价 */
    @TableField("high")
    private BigDecimal high;

    /** 最低价 */
    @TableField("low")
    private BigDecimal low;

    /** 收盘价 */
    @TableField("close")
    private BigDecimal close;

    /** 成交量（股/份） */
    @TableField("volume")
    private Long volume;

    /** 成交额 */
    @TableField("amount")
    private BigDecimal amount;

    /** 换手率 */
    @TableField("turnover_rate")
    private BigDecimal turnoverRate;

    /** 涨跌幅 */
    @TableField("pct_chg")
    private BigDecimal pctChg;

    /** 成交均价 */
    @TableField("average_price")
    private BigDecimal averagePrice;

    /** 是否停牌 */
    @TableField("suspension_flag")
    private Boolean suspensionFlag;

    /** 数据来源 */
    @TableField("source")
    private String source;

    /** 入库时间 */
    @TableField("ingested_time")
    private LocalDateTime ingestedTime;
}

