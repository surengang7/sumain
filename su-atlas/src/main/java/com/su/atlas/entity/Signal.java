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
 * 策略信号（买卖/止损/止盈等）
 */
@Data
@Accessors(chain = true)
@TableName("signal")
public class Signal implements Serializable {

    @Serial
    private static final long serialVersionUID = -4054117517896136892L;

    /** 主键 */
    @TableId(value = "signal_id", type = IdType.AUTO)
    private Long signalId;

    /** 策略运行ID */
    @TableField("run_id")
    private UUID runId;

    /** 证券ID */
    @TableField("security_id")
    private Long securityId;

    /** 信号时间 */
    @TableField("ts")
    private LocalDateTime ts;

    /** 信号类型（由代码枚举控制） */
    @TableField("signal_type")
    private String signalType;

    /** 信号强度（0~100） */
    @TableField("strength")
    private Integer strength;

    /** 触发参考价（可空） */
    @TableField("price_ref")
    private BigDecimal priceRef;

    /** 触发原因（可空） */
    @TableField("reason")
    private String reason;

    /** 扩展信息（JSON） */
    @TableField("meta")
    private String meta;

    /** 创建时间 */
    @TableField("created_time")
    private LocalDateTime createdTime;
}

