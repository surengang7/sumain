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
 * 资讯/公告/研报等内容（按 publish_time 月分区）
 */
@Data
@Accessors(chain = true)
@TableName("news_item")
public class NewsItem implements Serializable {

    @Serial
    private static final long serialVersionUID = -1370198177061992805L;

    /** 主键（UUID，建议代码生成或数据库生成） */
    @TableId(value = "news_id", type = IdType.INPUT)
    private UUID newsId;

    /** 来源：eastmoney/cls/ths 等 */
    @TableField("source")
    private String source;

    /** 来源侧唯一ID（用于去重/溯源） */
    @TableField("source_item_id")
    private String sourceItemId;

    /** 原文链接 */
    @TableField("url")
    private String url;

    /** 标题 */
    @TableField("title")
    private String title;

    /** 摘要 */
    @TableField("digest")
    private String digest;

    /** 正文引用（对象存储/文件路径等） */
    @TableField("content_ref")
    private String contentRef;

    /** 正文（小量可直接存，量大建议外置） */
    @TableField("content_text")
    private String contentText;

    /** 发布时间（建议UTC） */
    @TableField("publish_time")
    private LocalDateTime publishTime;

    /** 抓取时间 */
    @TableField("fetch_time")
    private LocalDateTime fetchTime;

    /** 语言 */
    @TableField("language")
    private String language;

    /** 作者（可空） */
    @TableField("author")
    private String author;

    /** 内容hash（用于去重） */
    @TableField("hash")
    private String hash;

    /** 资讯类型（由代码枚举控制） */
    @TableField("news_type")
    private String newsType;

    /** 扩展字段（JSON） */
    @TableField("extra")
    private String extra;
}

