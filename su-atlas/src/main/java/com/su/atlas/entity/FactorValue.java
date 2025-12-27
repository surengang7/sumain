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
 * 因子/指标结果（长表，便于扩展与回放）
 * 主键：(factor_name, security_id, ts, calc_version)
 */
@Data
@Accessors(chain = true)
@TableName("factor_value")
public class FactorValue implements Serializable {

    @Serial
    private static final long serialVersionUID = 4757003971591876580L;

    /** 因子名称（如MA20/RSI14/动量等） */
    @TableField("factor_name")
    private String factorName;

    /** 证券ID */
    @TableField("security_id")
    private Long securityId;

    /** 对齐时间（与bar时间一致） */
    @TableField("ts")
    private LocalDateTime ts;

    /** 因子值 */
    @TableField("value")
    private BigDecimal value;

    /** 计算版本（便于重算/对比） */
    @TableField("calc_version")
    private String calcVersion;

    /** 创建时间 */
    @TableField("created_time")
    private LocalDateTime createdTime;
}

