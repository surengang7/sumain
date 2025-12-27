package com.su.atlas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 成交回报/撮合结果（一笔订单可多笔成交）
 */
@Data
@Accessors(chain = true)
@TableName("trade_fill")
public class TradeFill implements Serializable {

    @Serial
    private static final long serialVersionUID = 5993505185441582620L;

    /** 主键（UUID） */
    @TableId(value = "fill_id", type = IdType.INPUT)
    private UUID fillId;

    /** 订单ID */
    @TableField("order_id")
    private UUID orderId;

    /** 成交时间 */
    @TableField("ts")
    private LocalDateTime ts;

    /** 成交价 */
    @TableField("price")
    private BigDecimal price;

    /** 成交量 */
    @TableField("qty")
    private Long qty;

    /** 手续费 */
    @TableField("fee")
    private BigDecimal fee;

    /** 税费 */
    @TableField("tax")
    private BigDecimal tax;

    /** 交易所成交编号（可空） */
    @TableField("exchange_trade_id")
    private String exchangeTradeId;

    /** 扩展信息（JSON） */
    @TableField("extra")
    private String extra;
}

