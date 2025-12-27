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
 * 资讯衍生特征（情绪/主题/重要度/关键词等）
 */
@Data
@Accessors(chain = true)
@TableName("news_feature")
public class NewsFeature implements Serializable {

    @Serial
    private static final long serialVersionUID = 7211394576992138613L;

    /** 资讯ID */
    @TableField("news_id")
    private String newsId;

    /** 情绪分值（-1~1，可空） */
    @TableField("sentiment")
    private BigDecimal sentiment;

    /** 主题分类（可空） */
    @TableField("topic")
    private String topic;

    /** 重要度分数（可空） */
    @TableField("importance")
    private Integer importance;

    /** 关键词列表（可空） - PG TEXT[] 建议后续自定义 TypeHandler */
    @TableField("keywords")
    private String[] keywords;

    /** 模型版本（可空） */
    @TableField("model_version")
    private String modelVersion;

    /** 创建时间 */
    @TableField("created_time")
    private LocalDateTime createdTime;
}

