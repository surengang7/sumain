package com.su.atlas.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SecurityService {



    void saveFromExcel(MultipartFile file) throws IOException;

}
