package com.sumain.matching.engine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketDepthLevel {
    private Integer level;        // 序号：1~5
    private BigDecimal price;     // 价格
    private Long quantity;        // 总数量
    private OrderDirection side;  // 方向
}