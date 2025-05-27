package com.sumain.matching.engine.model;

import lombok.Data;

import java.math.BigDecimal;


@Data
public abstract class BaseTrade {

    private BigDecimal price;         // 成交价格
    private Long quantity;            // 成交数量

}
