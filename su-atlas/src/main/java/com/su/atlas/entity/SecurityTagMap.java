package com.su.atlas.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 证券-标签多对多映射
 * 主键：(security_id, tag_id)
 */
@Data
@Accessors(chain = true)
@TableName("security_tag_map")
public class SecurityTagMap implements Serializable {

    @Serial
    private static final long serialVersionUID = -6528637965227508891L;

    /** 证券ID */
    @TableField("security_id")
    private Long securityId;

    /** 标签ID */
    @TableField("tag_id")
    private Long tagId;

    /** 映射权重/热度（可空） */
    @TableField("weight")
    private BigDecimal weight;

    /** 生效开始日期（可空） */
    @TableField("effective_from")
    private LocalDate effectiveFrom;

    /** 生效结束日期（可空） */
    @TableField("effective_to")
    private LocalDate effectiveTo;

    /** 创建时间 */
    @TableField("created_time")
    private LocalDateTime createdTime;
}

