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

    /** 主键（内部ID） */
    @TableId(value = "security_id", type = IdType.AUTO)
    private Long securityId;

    /** 证券代码：A股如600519/000001，港股如00700 */
    @TableField("security_code")
    private String securityCode;

    /** 市场：CN_A/HK/US 等 */
    @TableField("market")
    private String market;

    /** 所属交易所ID */
    @TableField("exchange_id")
    private Long exchangeId;

    /** 证券中文名 */
    @TableField("security_name")
    private String securityName;

    /** 证券英文名 */
    @TableField("security_name_en")
    private String securityNameEn;

    /** 证券类型：STOCK/ETF/INDEX 等（由代码枚举控制） */
    @TableField("security_type")
    private String securityType;

    /** 交易币种 */
    @TableField("currency")
    private String currency;

    /** 最小交易单位（A股通常100股） */
    @TableField("trade_min_size")
    private Integer tradeMinSize;

    /** 上市日期 */
    @TableField("listing_date")
    private LocalDate listingDate;

    /** 退市日期 */
    @TableField("delisting_date")
    private LocalDate delistingDate;

    /** 状态：LISTED/SUSPENDED/DELISTED 等（由代码枚举控制） */
    @TableField("security_status")
    private String securityStatus;

    /** 是否支持融资融券（可空） */
    @TableField("margin_yn")
    private Boolean marginYn;

    /** 创建时间 */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /** 更新时间 */
    @TableField("updated_time")
    private LocalDateTime updatedTime;
}

