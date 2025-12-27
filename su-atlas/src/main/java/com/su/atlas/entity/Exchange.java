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
 * 交易所/市场定义
 */
@Data
@Accessors(chain = true)
@TableName("exchange")
public class Exchange implements Serializable {

    @Serial
    private static final long serialVersionUID = 5801008084339444605L;

    /** 主键 */
    @TableId(value = "exchange_id", type = IdType.AUTO)
    private Long exchangeId;

    /** 交易所代码：SSE/SZSE/HKEX/NYSE 等 */
    @TableField("exchange_code")
    private String exchangeCode;

    /** 交易所名称 */
    @TableField("exchange_name")
    private String exchangeName;

    /** 交易所时区 */
    @TableField("exchange_timezone")
    private String exchangeTimezone;

    /** 创建时间 */
    @TableField("created_time")
    private LocalDateTime createdTime;
}

