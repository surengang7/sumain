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
 * 资金快照（资产、可用资金、回撤等）
 * 主键：(account_id, ts)
 */
@Data
@Accessors(chain = true)
@TableName("cash_snapshot")
public class CashSnapshot implements Serializable {

    @Serial
    private static final long serialVersionUID = 6807438061184221492L;

    /** 账户ID */
    @TableField("account_id")
    private Long accountId;

    /** 快照时间 */
    @TableField("ts")
    private LocalDateTime ts;

    /** 可用现金 */
    @TableField("cash")
    private BigDecimal cash;

    /** 冻结资金 */
    @TableField("frozen_cash")
    private BigDecimal frozenCash;

    /** 账户总权益 */
    @TableField("equity")
    private BigDecimal equity;

    /** 回撤（可空） */
    @TableField("drawdown")
    private BigDecimal drawdown;
}

