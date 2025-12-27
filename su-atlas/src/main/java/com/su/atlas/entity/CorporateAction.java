package com.su.atlas.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 公司行为（分红送转拆并股等）
 */
@Data
@Accessors(chain = true)
@TableName("corporate_action")
public class CorporateAction implements Serializable {

    @Serial
    private static final long serialVersionUID = -6785271984100638622L;

    /** 主键 */
    @TableId(value = "action_id", type = IdType.AUTO)
    private Long actionId;

    /** 证券ID */
    @TableField("security_id")
    private Long securityId;

    /** 发生日期 */
    @TableField("action_date")
    private LocalDate actionDate;

    /** 公司行为类型（由代码枚举控制） */
    @TableField("action_type")
    private String actionType;

    /** 每股现金分红（可空） */
    @TableField("cash_per_share")
    private BigDecimal cashPerShare;

    /** 比例：拆分/送股/合并等（可空） */
    @TableField("ratio")
    private BigDecimal ratio;

    /** 扩展信息（JSON） */
    @TableField("detail")
    private String detail;

    /** 数据来源 */
    @TableField("source")
    private String source;

    /** 入库时间 */
    @TableField("ingested_time")
    private LocalDateTime ingestedTime;
}

