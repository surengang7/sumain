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
 * 原始数据载荷（便于溯源与重放解析；量大可只存引用）
 */
@Data
@Accessors(chain = true)
@TableName("raw_payload")
public class RawPayload implements Serializable {

    @Serial
    private static final long serialVersionUID = 4269378166610258884L;

    /** 主键（UUID） */
    @TableId(value = "payload_id", type = IdType.INPUT)
    private UUID payloadId;

    /** 数据源ID */
    @TableField("source_id")
    private Long sourceId;

    /** 实体类型（market_daily/NEWS等，由代码枚举控制） */
    @TableField("entity_type")
    private String entityType;

    /** 实体键（如security_id|date等） */
    @TableField("entity_key")
    private String entityKey;

    /** 抓取时间 */
    @TableField("fetch_time")
    private LocalDateTime fetchTime;

    /** 原始payload（JSON） */
    @TableField("payload")
    private String payload;
}

