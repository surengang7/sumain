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
 * 复权因子（用于前复权/后复权计算，不覆盖原始价格）
 * 主键：(security_id, trade_date)
 */
@Data
@Accessors(chain = true)
@TableName("adj_factor")
public class AdjFactor implements Serializable {

    @Serial
    private static final long serialVersionUID = -1175890256026761357L;

    /** 证券ID */
    @TableField("security_id")
    private Long securityId;

    /** 交易日 */
    @TableField("trade_date")
    private LocalDate tradeDate;

    /** 复权因子 */
    @TableField("factor")
    private BigDecimal factor;

    /** 数据来源 */
    @TableField("source")
    private String source;

    /** 入库时间 */
    @TableField("ingested_time")
    private LocalDateTime ingestedTime;
}

