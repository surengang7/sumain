package com.sunline.compare.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlCompareReport {

    private String type;
    private String message;
    private String xpath;    // 节点路径
    private String sourceValue;   // 源值
    private String targetValue;   // 目标值
}
