package com.su.atlas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 风控事件（触发止损/超限/异常等）
 */
@Data
@Accessors(chain = true)
@TableName("risk_event")
public class RiskEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = -2965716841087788168L;

    /** 主键 */
    @TableId(value = "event_id", type = IdType.AUTO)
    private Long eventId;

    /** 账户ID */
    @TableField("account_id")
    private Long accountId;

    /** 事件时间 */
    @TableField("ts")
    private LocalDateTime ts;

    /** 事件类型（由代码枚举控制） */
    @TableField("risk_type")
    private String riskType;

    /** 事件详情（JSON） */
    @TableField("detail")
    private String detail;
}

