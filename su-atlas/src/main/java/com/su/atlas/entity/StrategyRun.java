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
import java.util.UUID;

/**
 * 策略运行实例（回测/模拟/实盘一次运行）
 */
@Data
@Accessors(chain = true)
@TableName("strategy_run")
public class StrategyRun implements Serializable {

    @Serial
    private static final long serialVersionUID = 8757523501874751624L;

    /** 主键（UUID） */
    @TableId(value = "run_id", type = IdType.INPUT)
    private UUID runId;

    /** 策略ID */
    @TableField("strategy_id")
    private Long strategyId;

    /** 运行模式（由代码枚举控制） */
    @TableField("mode")
    private String mode;

    /** 运行开始时间（可空） */
    @TableField("start_time")
    private LocalDateTime startTime;

    /** 运行结束时间（可空） */
    @TableField("end_time")
    private LocalDateTime endTime;

    /** 本次运行参数（JSON） */
    @TableField("params")
    private String params;

    /** 数据快照/版本信息（JSON） */
    @TableField("data_snapshot")
    private String dataSnapshot;

    /** 运行状态 */
    @TableField("status")
    private String status;

    /** 日志引用（可空） */
    @TableField("log_ref")
    private String logRef;

    /** 创建时间 */
    @TableField("created_time")
    private LocalDateTime createdTime;
}

