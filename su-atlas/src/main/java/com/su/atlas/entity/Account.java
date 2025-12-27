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
 * 账户（模拟/实盘）
 */
@Data
@Accessors(chain = true)
@TableName("account")
public class Account implements Serializable {

    @Serial
    private static final long serialVersionUID = -5572260686842957597L;

    /** 主键 */
    @TableId(value = "account_id", type = IdType.AUTO)
    private Long accountId;

    /** 券商/通道标识（模拟也可） */
    @TableField("broker")
    private String broker;

    /** 账户模式（由代码枚举控制） */
    @TableField("mode")
    private String mode;

    /** 基准币种 */
    @TableField("base_currency")
    private String baseCurrency;

    /** 创建时间 */
    @TableField("created_time")
    private LocalDateTime createdTime;
}

