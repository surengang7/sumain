package com.sumain.feign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumain.common.entity.ResponseEntity;
import com.sumain.common.enums.ResponseEnum;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 500) {
            try {
                // 使用 InputStream 获取 Feign 响应体内容
                InputStream inputStream = response.body().asInputStream();

                TypeReference<ResponseEntity<Void>> typeRef = new TypeReference<>() {};
                ResponseEntity<Void> responseEntity = objectMapper.readValue(inputStream, typeRef);

                if(!Objects.equals(responseEntity.getCode(), ResponseEnum.SUCCESS.getCode())){
                    // 创建 FeignException.InternalServerError 异常对象
                    return new FeignException.InternalServerError(
                            responseEntity.getMsg(),
                            response.request(),
                            responseEntity.getMsg().getBytes(),
                            response.headers()
                    );
                }
            } catch (IOException e) {
                log.error("反序列化异常：",e);
                // 如果反序列化失败，则返回默认异常
                return defaultDecoder.decode(methodKey, response);
            }
        }
        return defaultDecoder.decode(methodKey, response);
    }
}
