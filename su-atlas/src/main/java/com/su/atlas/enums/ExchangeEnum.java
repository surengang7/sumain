package com.su.atlas.enums;


import com.su.atlas.exception.GeneralException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExchangeEnum {

    // ===== 中国大陆 =====
    SSE("SSE", "上海证券交易所", "Asia/Shanghai"),
    SZSE("SZSE", "深圳证券交易所", "Asia/Shanghai"),
    BSE("BSE", "北京证券交易所", "Asia/Shanghai"),

    // ===== 中国香港 =====
    HKEX("HKEX", "香港交易所", "Asia/Hong_Kong"),

    // ===== 美国 =====
    NYSE("NYSE", "纽约证券交易所", "America/New_York"),
    NASDAQ("NASDAQ", "纳斯达克证券交易所", "America/New_York"),

    // ===== 日本（预留）=====
    TSE("TSE", "东京证券交易所", "Asia/Tokyo");

    /** 数据库存储的 code */
    private final String code;

    /** 中文名称 */
    private final String name;

    /** 交易所时区 */
    private final String timezone;

    public static ExchangeEnum ofCode(String code){
        for (ExchangeEnum value : values()) {
            if(value.getCode().equals(code)) return value;
        }
        throw new GeneralException("invalid exchange code");
    }
}
