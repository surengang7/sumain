package com.su.atlas.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 资讯全局去重索引表（跨分区保证 source+source_item_id 唯一）
 * 主键：(source, source_item_id)
 */
@Data
@Accessors(chain = true)
@TableName("news_deduplicat")
public class NewsDeduplicat implements Serializable {

    @Serial
    private static final long serialVersionUID = 917354466727992788L;

    /** 来源 */
    @TableField("source")
    private String source;

    /** 来源侧唯一ID */
    @TableField("source_item_id")
    private String sourceItemId;

    /** 对应 news_item.news_id */
    @TableField("news_id")
    private UUID newsId;

    /** 创建时间 */
    @TableField("created_time")
    private LocalDateTime createdTime;
}

