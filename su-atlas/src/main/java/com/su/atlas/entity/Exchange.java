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

    @TableId(value = "exchange_id", type = IdType.ASSIGN_ID)
    private Long exchangeId;            // 主键

    private String exchangeCode;         // 交易所代码：SSE/SZSE/HKEX/NYSE 等
    private String exchangeName;         // 交易所名称
    private String exchangeTimezone;     // 交易所时区
    private LocalDateTime createdTime;   // 创建时间
}

