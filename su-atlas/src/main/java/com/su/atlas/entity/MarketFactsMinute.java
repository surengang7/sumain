package com.su.atlas.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 分钟线行情（1分钟粒度，按 ts 月分区）
 * 主键：(security_id, ts)
 *
 * 注意：这是分区父表实体；子分区表不需要单独实体
 */
@Data
@Accessors(chain = true)
@TableName("market_facts_minute")
public class MarketFactsMinute implements Serializable {

    @Serial
    private static final long serialVersionUID = 3393017511554956452L;

    /** 证券ID */
    @TableField("security_id")
    private Long securityId;

    /** 时间戳（分钟） */
    @TableField("ts")
    private LocalDateTime ts;

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

    /** 成交量 */
    @TableField("volume")
    private Long volume;

    /** 成交额 */
    @TableField("amount")
    private BigDecimal amount;

    /** 成交均价 */
    @TableField("average_price")
    private BigDecimal averagePrice;

    /** 数据来源 */
    @TableField("source")
    private String source;

    /** 入库时间 */
    @TableField("ingested_time")
    private LocalDateTime ingestedTime;
}

