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
 * 订单（策略或手动下单）
 */
@Data
@Accessors(chain = true)
@TableName("orders")
public class Orders implements Serializable {

    @Serial
    private static final long serialVersionUID = -4593915416538388561L;

    /** 主键（UUID） */
    @TableId(value = "order_id", type = IdType.INPUT)
    private UUID orderId;

    /** 账户ID */
    @TableField("account_id")
    private Long accountId;

    /** 策略运行ID（可空） */
    @TableField("run_id")
    private UUID runId;

    /** 证券ID */
    @TableField("security_id")
    private Long securityId;

    /** 下单时间 */
    @TableField("ts")
    private LocalDateTime ts;

    /** 买卖方向（由代码枚举控制） */
    @TableField("side")
    private String side;

    /** 订单类型（由代码枚举控制） */
    @TableField("order_type")
    private String orderType;

    /** 委托价（市价单可空） */
    @TableField("price")
    private BigDecimal price;

    /** 委托数量 */
    @TableField("qty")
    private Long qty;

    /** 订单状态（由代码枚举控制） */
    @TableField("status")
    private String status;

    /** 订单来源：STRATEGY/MANUAL 等 */
    @TableField("source")
    private String source;

    /** 下单原因（可空） */
    @TableField("reason")
    private String reason;

    /** 扩展信息（JSON） */
    @TableField("extra")
    private String extra;
}

