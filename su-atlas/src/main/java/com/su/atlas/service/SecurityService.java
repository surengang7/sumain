package com.su.atlas.service;





import com.su.atlas.entity.Security;

import java.util.List;

public interface SecurityService {



    void saveBatch(List<Security> securityList,Boolean updateYn,int batchSize);

}
