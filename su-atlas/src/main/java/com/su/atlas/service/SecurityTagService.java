package com.su.atlas.service;

import com.su.atlas.entity.SecurityTag;
import java.util.List;

public interface SecurityTagService {

    List<SecurityTag> selectAll();

    SecurityTag selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(SecurityTag entity);

    boolean insert(SecurityTag entity);

}
