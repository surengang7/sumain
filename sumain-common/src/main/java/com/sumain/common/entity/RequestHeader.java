package com.sumain.common.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class RequestHeader {

    public static final String USER_ID = "X-User-Id";
    public static final String USER_NAME = "X-User-Name";
    public static final String LOGIN_TOKEN = "Authorization";
    public static final String SYSTEM_TYPE = "System-Type";
    public static final String EXCHANGE_ID = "Exchange-Id";
    public static final String COMPANY_ID = "Company-Id";
    public static final String TRACE_ID = "X-Trace-Id";

    public Map<String,String> requestHeaderMap = new HashMap<>();

    public void setUserName(String userName) {
        requestHeaderMap.putIfAbsent(USER_NAME, userName);
    }

    public String getUserName() {
        if(requestHeaderMap.get(USER_NAME) == null) return null;
        return requestHeaderMap.get(USER_NAME);
    }

    public void setUserId(Long userId) {
        requestHeaderMap.putIfAbsent(USER_ID, userId.toString());
    }

    public Long getUserId() {
        if(requestHeaderMap.get(USER_ID) == null) return null;
        return Long.parseLong(requestHeaderMap.get(USER_ID));
    }

    public void setLoginToken(String token) {
        requestHeaderMap.putIfAbsent(LOGIN_TOKEN, token);
    }

    public String getLoginToken() {
        return requestHeaderMap.get(LOGIN_TOKEN);
    }

    public void setSystemType(String systemType) {
        requestHeaderMap.putIfAbsent(SYSTEM_TYPE, systemType);
    }

    public String getSystemType() {
        return requestHeaderMap.get(SYSTEM_TYPE);
    }

    public void setExchangeId(Long exchangeId) {
        requestHeaderMap.putIfAbsent(EXCHANGE_ID, exchangeId.toString());
    }

    public Long getExchangeId() {
        if(requestHeaderMap.get(EXCHANGE_ID) == null) return null;
        return Long.parseLong(requestHeaderMap.get(EXCHANGE_ID));
    }

    public void setCompanyId(Long companyId) {
        requestHeaderMap.putIfAbsent(COMPANY_ID, companyId.toString());
    }

    public Long getCompanyId() {
        if(requestHeaderMap.get(COMPANY_ID) == null) return null;
        return Long.parseLong(requestHeaderMap.get(COMPANY_ID));
    }

    public void setTraceId(String traceId){
        requestHeaderMap.putIfAbsent(TRACE_ID, traceId);
    }

    public String getTraceId(){
        return requestHeaderMap.get(TRACE_ID);
    }



}
