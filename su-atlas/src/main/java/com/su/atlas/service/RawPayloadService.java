package com.su.atlas.service;

import com.su.atlas.entity.RawPayload;
import java.util.List;

public interface RawPayloadService {

    List<RawPayload> selectAll();

    RawPayload selectById(UUID id);

    boolean deleteById(UUID id);

    boolean updateById(RawPayload entity);

    boolean insert(RawPayload entity);

}
