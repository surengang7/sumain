package com.sumain.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.List;

public class ObjectUtils {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(T source) throws JsonProcessingException {
        if (source == null) {
            return null;
        }
        // 将对象转换为JSON字符串
        String json = objectMapper.writeValueAsString(source);
        // 将JSON字符串转换为对象
        return (T) objectMapper.readValue(json, source.getClass());
    }

    public static <T> List<T> deepCopyList(List<T> source) throws JsonProcessingException {
        if (source == null) {
            return null;
        }
        // 将集合转换为JSON字符串
        String json = objectMapper.writeValueAsString(source);
        // 将JSON字符串转换为集合
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, source.get(0).getClass()));
    }
}
