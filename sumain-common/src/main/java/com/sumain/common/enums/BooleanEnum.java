package com.sumain.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum BooleanEnum {

    Y(1,true,"是"),
    N(0,false,"否"),
    ;
    private final Integer code;
    private final Boolean value;
    private final String msg;


    public static BooleanEnum parseCode(Integer code){
        for (BooleanEnum value : values()) {
            if(Objects.equals(value.getCode(), code)) return value;
        }
        return null;
    }

    public static BooleanEnum parseValue(Boolean value){
        for (BooleanEnum v : values()) {
            if(Objects.equals(v.getValue(), value)) return v;
        }
        return null;
    }

    public static BooleanEnum parseMsg(String msg){
        for (BooleanEnum value : values()) {
            if(Objects.equals(value.getMsg(), msg)) return value;
        }
        return null;
    }
}
