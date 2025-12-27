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
 * 持仓快照（建议日终/分钟，用于回测与风控）
 * 主键：(account_id, ts, security_id)
 */
@Data
@Accessors(chain = true)
@TableName("position_snapshot")
public class PositionSnapshot implements Serializable {

    @Serial
    private static final long serialVersionUID = 5596413010163356265L;

    /** 账户ID */
    @TableField("account_id")
    private Long accountId;

    /** 快照时间 */
    @TableField("ts")
    private LocalDateTime ts;

    /** 证券ID */
    @TableField("security_id")
    private Long securityId;

    /** 持仓数量 */
    @TableField("qty")
    private Long qty;

    /** 持仓均价（可空） */
    @TableField("avg_cost")
    private BigDecimal avgCost;

    /** 市价（可空） */
    @TableField("market_price")
    private BigDecimal marketPrice;

    /** 市值（可空） */
    @TableField("market_value")
    private BigDecimal marketValue;

    /** 浮动盈亏（可空） */
    @TableField("unrealized_pnl")
    private BigDecimal unrealizedPnl;

    /** 已实现盈亏（可空） */
    @TableField("realized_pnl")
    private BigDecimal realizedPnl;
}

