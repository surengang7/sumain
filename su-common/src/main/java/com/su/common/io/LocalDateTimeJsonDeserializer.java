package com.su.common.io;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.su.common.utils.DateUtils;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeJsonDeserializer extends JsonDeserializer<LocalDateTime> {


    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return DateUtils.toLocalDateTime(p.getText());
    }
}
