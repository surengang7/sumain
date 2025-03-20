package com.sumain.common.entity;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalContext {

    public static final String REQUEST_HEADER = "request_header";

    private static final ThreadLocal<Map<String, Object>> THREAD_LOCAL = ThreadLocal.withInitial(HashMap::new);

    public static void set(String key, Object value) {
        THREAD_LOCAL.get().put(key, value);
    }

    public static Object get(String key) {
        return THREAD_LOCAL.get().get(key);
    }

    public static void remove(String key) {
        THREAD_LOCAL.get().remove(key);
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }

    public static RequestHeader getRequestHeader(){
        return (RequestHeader) THREAD_LOCAL.get().get(REQUEST_HEADER);
    }

}
