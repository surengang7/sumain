package com.sumain.matching.engine.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public abstract class BaseOrder {

    private OrderDirection side;                        // 买卖方向
    private BigDecimal price;                           // 挂单价格
    private Long quantity;                              // 挂单数量

    /**
     * 获取剩余数量
     */
    public abstract Long getRemainingQuantity();

    /**
     * 更新一些属性
     */
    public abstract void update(Long currentFillQuantity,BigDecimal currentFillPrice);

    /**
     * 获取产品/标的唯一id
     */
    public abstract String getProductCode();

    /**
     * 获取挂单时间
     */
    public abstract LocalDateTime getOrderTime();

    /**
     * 更新状态
     */
    public abstract void updateStatus();

    /**
     * 获取挂单编号
     */
    public abstract String getOrderNo();

    /**
     * 获取用户id
     */
    public abstract String getUserNo();

}
