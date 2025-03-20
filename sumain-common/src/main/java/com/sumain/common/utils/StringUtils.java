package com.sumain.common.utils;

import cn.hutool.core.util.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class StringUtils {

    // 合并字符串,delimiter作为分隔符
    public static String joinStrings(List<String> stringList, String delimiter) {
        if (stringList == null || stringList.isEmpty()) {
            return null;
        }
        return String.join(delimiter, stringList);
    }

    // 删除相邻的重复项
    public static List<String> removeAdjacentDuplicates(List<String> inputList) {
        if (inputList == null || inputList.isEmpty()) {
            return inputList;
        }
        List<String> resultList = new ArrayList<>();
        String previous = null;

        for (String current : inputList) {
            if (!current.equals(previous)) {
                resultList.add(current);
            }
            previous = current;
        }
        return resultList;
    }

    // string -> object 转换器
    public static <T> T convert(String value, Function<String, T> function){
        if(isEmpty(value)) return null;
        return function.apply(value);
    }

    public static Boolean isEmpty(String value){
        return value == null || value.isEmpty() || value.equals("null");
    }

    public static Boolean isNotEmpty(String value){
        return StrUtil.isNotEmpty(value);
    }
}
