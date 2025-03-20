package com.sumain.common.io;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.sumain.common.utils.DateUtils;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeJsonSerializer extends JsonSerializer<LocalDateTime>{
    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(DateUtils.format(localDateTime));
    }
}
