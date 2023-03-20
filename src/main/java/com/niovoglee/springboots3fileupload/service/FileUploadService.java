package com.niovoglee.springboots3fileupload.service;

import com.amazonaws.services.s3.model.Bucket;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileUploadService {

    String uploadFile(MultipartFile file);

    List<Bucket> listBucket();

    boolean deleteBucket(String bucketName);
}
