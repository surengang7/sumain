package com.su.atlas.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 策略输出的目标仓位（组合权重/目标股数）
 * 主键：(run_id, ts, security_id)
 */
@Data
@Accessors(chain = true)
@TableName("position_target")
public class PositionTarget implements Serializable {

    @Serial
    private static final long serialVersionUID = 1593926410250795893L;

    /** 策略运行ID */
    @TableField("run_id")
    private UUID runId;

    /** 目标时间 */
    @TableField("ts")
    private LocalDateTime ts;

    /** 证券ID */
    @TableField("security_id")
    private Long securityId;

    /** 目标权重（0~1） */
    @TableField("target_weight")
    private BigDecimal targetWeight;

    /** 目标股数（可空） */
    @TableField("target_shares")
    private Long targetShares;

    /** 备注（可空） */
    @TableField("comment")
    private String comment;
}

