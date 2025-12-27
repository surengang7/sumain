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
 * 采集运行日志（用于排错/补数/监控）
 */
@Data
@Accessors(chain = true)
@TableName("ingest_log")
public class IngestLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 338558055055823645L;

    /** 主键 */
    @TableId(value = "log_id", type = IdType.AUTO)
    private Long logId;

    /** 任务ID */
    @TableField("job_id")
    private Long jobId;

    /** 开始时间 */
    @TableField("start_time")
    private LocalDateTime startTime;

    /** 结束时间（可空） */
    @TableField("end_time")
    private LocalDateTime endTime;

    /** 运行状态 */
    @TableField("status")
    private String status;

    /** 输入记录数（可空） */
    @TableField("records_in")
    private Long recordsIn;

    /** 输出记录数（可空） */
    @TableField("records_out")
    private Long recordsOut;

    /** 错误信息（可空） */
    @TableField("error_msg")
    private String errorMsg;

    /** 增量检查点（JSON，可空） */
    @TableField("checkpoint")
    private String checkpoint;
}

