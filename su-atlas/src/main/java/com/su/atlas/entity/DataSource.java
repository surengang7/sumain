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
 * 数据源定义（便于扩展/溯源/限流管理）
 */
@Data
@Accessors(chain = true)
@TableName("data_source")
public class DataSource implements Serializable {

    @Serial
    private static final long serialVersionUID = 4680988697090661024L;

    /** 主键 */
    @TableId(value = "source_id", type = IdType.AUTO)
    private Long sourceId;

    /** 数据源名称 */
    @TableField("name")
    private String name;

    /** 数据源类型 */
    @TableField("source_type")
    private String sourceType;

    /** 鉴权类型（可空） */
    @TableField("auth_type")
    private String authType;

    /** 限流（可空） */
    @TableField("rate_limit")
    private Integer rateLimit;

    /** 备注（可空） */
    @TableField("notes")
    private String notes;

    /** 创建时间 */
    @TableField("created_time")
    private LocalDateTime createdTime;
}

