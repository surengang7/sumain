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
 * 资讯-证券关联（多对多）
 * 主键：(news_id, security_id)
 */
@Data
@Accessors(chain = true)
@TableName("news_security_map")
public class NewsSecurityMap implements Serializable {

    @Serial
    private static final long serialVersionUID = 4081240341533559295L;

    /** 资讯ID */
    @TableField("news_id")
    private UUID newsId;

    /** 证券ID */
    @TableField("security_id")
    private Long securityId;

    /** 关联方式（由代码枚举控制） */
    @TableField("match_type")
    private String matchType;

    /** 关联置信度 */
    @TableField("confidence")
    private BigDecimal confidence;

    /** 创建时间 */
    @TableField("created_time")
    private LocalDateTime createdTime;
}

