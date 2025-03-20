package com.sumain.feign;

import com.sumain.common.entity.RequestHeader;
import com.sumain.common.entity.ThreadLocalContext;
import com.sumain.common.utils.StringUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // 从 ThreadLocal 获取信息
        RequestHeader requestHeader = ThreadLocalContext.getRequestHeader();
        if(requestHeader == null) return;

        log.info("feign interceptor set request header,requestHeader = {}",requestHeader.getRequestHeaderMap());

        // 将信息添加到请求头
        if (requestHeader.getUserId() != null) {
            template.header(RequestHeader.USER_ID, requestHeader.getUserId().toString());
        }
        if (StringUtils.isNotEmpty(requestHeader.getLoginToken())) {
            template.header(RequestHeader.LOGIN_TOKEN, requestHeader.getLoginToken());
        }
        if (StringUtils.isNotEmpty(requestHeader.getSystemType())) {
            template.header(RequestHeader.SYSTEM_TYPE, requestHeader.getSystemType());
        }
        if (requestHeader.getExchangeId() != null) {
            template.header(RequestHeader.EXCHANGE_ID, requestHeader.getExchangeId().toString());
        }
        if (requestHeader.getCompanyId() != null) {
            template.header(RequestHeader.COMPANY_ID, requestHeader.getCompanyId().toString());
        }
        if (StringUtils.isNotEmpty(requestHeader.getTraceId())) {
            template.header(RequestHeader.TRACE_ID, requestHeader.getTraceId());
        }
        if (StringUtils.isNotEmpty(requestHeader.getUserName())) {
            template.header(RequestHeader.USER_NAME, URLEncoder.encode(requestHeader.getUserName(), StandardCharsets.UTF_8));
        }
    }
}
