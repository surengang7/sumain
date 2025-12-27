package com.su.atlas.service;

import com.su.atlas.entity.CorporateAction;
import java.util.List;

public interface CorporateActionService {

    List<CorporateAction> selectAll();

    CorporateAction selectById(Long id);

    boolean deleteById(Long id);

    boolean updateById(CorporateAction entity);

    boolean insert(CorporateAction entity);

}
