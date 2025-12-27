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
 * 策略定义（参数结构、代码版本、选股范围等）
 */
@Data
@Accessors(chain = true)
@TableName("strategy")
public class Strategy implements Serializable {

    @Serial
    private static final long serialVersionUID = 4666195375096028940L;

    /** 主键 */
    @TableId(value = "strategy_id", type = IdType.AUTO)
    private Long strategyId;

    /** 策略名称 */
    @TableField("strategy_name")
    private String strategyName;

    /** 策略描述 */
    @TableField("strategy_description")
    private String strategyDescription;

    /** 股票池/范围定义（如ALL/HS300/自选等） */
    @TableField("universe")
    private String universe;

    /** 行情粒度：1D/1M */
    @TableField("bar_type")
    private String barType;

    /** 参数定义结构（JSON） */
    @TableField("params_schema")
    private String paramsSchema;

    /** 策略代码版本引用（git commit/tag等） */
    @TableField("code_ref")
    private String codeRef;

    /** 创建时间 */
    @TableField("created_time")
    private LocalDateTime createdTime;
}

