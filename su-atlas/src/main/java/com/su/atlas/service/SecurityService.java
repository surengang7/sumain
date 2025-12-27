package com.su.atlas.service;

import com.su.atlas.entity.Security;
import java.util.List;

public interface SecurityService {

    List<Security> selectAll();

    Security selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(Security entity);

    boolean insert(Security entity);

}
