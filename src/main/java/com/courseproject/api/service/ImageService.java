package com.courseproject.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ImageService {

    String uploadToCloud(MultipartFile image) throws IOException;

    void delete(Long id) throws IOException;

}
