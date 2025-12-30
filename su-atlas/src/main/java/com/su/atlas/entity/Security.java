package com.su.atlas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 证券主表（股票/ETF/指数等统一抽象）
 */
@Data
@Accessors(chain = true)
@TableName("security")
public class Security implements Serializable {

    @Serial
    private static final long serialVersionUID = -871820710156041810L;

    @TableId(value = "security_id", type = IdType.ASSIGN_ID)
    private Long securityId;                   // 主键

    private String securityCode;               // 证券代码：A股如600519/000001，港股如00700
    private String market;                     // 市场：CN_A/HK/US 等
    private Long exchangeId;                   // 所属交易所ID
    private String securityName;               // 证券中文名
    private String securityNameEn;             // 证券英文名
    private String securityType;               // 证券类型：STOCK/ETF/INDEX 等（由代码枚举控制）
    private String currency;                   // 交易币种
    private Integer tradeMinSize;              // 最小交易单位（A股通常100股）
    private LocalDate listingDate;             // 上市日期
    private LocalDate delistingDate;           // 退市日期
    private String securityStatus;             // 状态：LISTED/SUSPENDED/DELISTED 等（由代码枚举控制）
    private Boolean marginYn;                  // 是否支持融资融券（可空）
    private LocalDateTime createdTime;         // 创建时间
    private LocalDateTime updatedTime;         // 更新时间

}

