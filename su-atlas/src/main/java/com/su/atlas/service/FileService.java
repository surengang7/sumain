package com.su.atlas.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    default void handlerMultipartFile(MultipartFile... files){

    };

    default void handlerMultipartFile(List<MultipartFile> files){

    };

}
