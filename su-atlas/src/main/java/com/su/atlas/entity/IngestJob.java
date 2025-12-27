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
 * 采集任务定义（定时/增量/全量）
 */
@Data
@Accessors(chain = true)
@TableName("ingest_job")
public class IngestJob implements Serializable {

    @Serial
    private static final long serialVersionUID = -7093696513624535547L;

    /** 主键 */
    @TableId(value = "job_id", type = IdType.AUTO)
    private Long jobId;

    /** 数据源ID */
    @TableField("source_id")
    private Long sourceId;

    /** 任务类型（由代码枚举控制） */
    @TableField("job_type")
    private String jobType;

    /** cron表达式（可空） */
    @TableField("schedule_cron")
    private String scheduleCron;

    /** 任务状态 */
    @TableField("status")
    private String status;

    /** 上次运行时间（可空） */
    @TableField("last_run_time")
    private LocalDateTime lastRunTime;

    /** 上次成功时间（可空） */
    @TableField("last_success_time")
    private LocalDateTime lastSuccessTime;

    /** 创建时间 */
    @TableField("created_time")
    private LocalDateTime createdTime;
}

