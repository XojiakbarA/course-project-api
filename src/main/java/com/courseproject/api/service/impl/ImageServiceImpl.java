package com.courseproject.api.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.courseproject.api.entity.Image;
import com.courseproject.api.repository.ImageRepository;
import com.courseproject.api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    Cloudinary cloudinary;

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public void delete(Long id) throws IOException {
        Image image = imageRepository.findById(id).orElse(null);
        try {
            if (image != null) {
                deleteFromCloud(image.getValue());
            }
        } catch (IOException e) {
            throw new IOException();
        }
        imageRepository.deleteById(id);
    }

    @Override
    public String uploadToCloud(MultipartFile image) throws IOException  {
        File file = convert(image);
        Map<?,?> result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        file.delete();
        return result.get("public_id").toString();
    }
    private void deleteFromCloud(String id) throws IOException {
        cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }

}
