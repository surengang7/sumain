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
 * 行业/概念/主题标签维表（口径由 taxonomy 区分）
 */
@Data
@Accessors(chain = true)
@TableName("security_tag")
public class SecurityTag implements Serializable {

    @Serial
    private static final long serialVersionUID = 7130260998241559213L;

    /** 主键 */
    @TableId(value = "tag_id", type = IdType.AUTO)
    private Long tagId;

    /** 标签口径：SW/THS/EM/CUSTOM 等 */
    @TableField("taxonomy")
    private String taxonomy;

    /** 标签类型：INDUSTRY/CONCEPT/THEME 等 */
    @TableField("tag_type")
    private String tagType;

    /** 标签代码（可空） */
    @TableField("tag_code")
    private String tagCode;

    /** 标签名称 */
    @TableField("tag_name")
    private String tagName;

    /** 创建时间 */
    @TableField("created_time")
    private LocalDateTime createdTime;
}

