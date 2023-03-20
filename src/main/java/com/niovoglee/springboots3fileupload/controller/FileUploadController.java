package com.niovoglee.springboots3fileupload.controller;

import com.amazonaws.services.s3.model.Bucket;
import com.niovoglee.springboots3fileupload.service.AWSS3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.niovoglee.springboots3fileupload.resource.FileUploadResource.API_FILE;
import static com.niovoglee.springboots3fileupload.resource.FileUploadResource.API_FILE_DELETE;
import static com.niovoglee.springboots3fileupload.resource.FileUploadResource.API_FILE_LIST;
import static com.niovoglee.springboots3fileupload.resource.FileUploadResource.API_FILE_SAVE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(API_FILE)
public class FileUploadController {

    @Autowired
    private AWSS3UploadService awsS3Service;

    @PostMapping(API_FILE_SAVE)
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {

        String publicURL = awsS3Service.uploadFile(file);
        Map<String, String> response = new HashMap<>();
        response.put("publicURL", publicURL);
        return new ResponseEntity<Map<String, String>>(response, CREATED);
    }

    @GetMapping(API_FILE_LIST)
    public ResponseEntity<List<Bucket>> listFile() {

        List<Bucket> list = awsS3Service.listBucket();
        return new ResponseEntity<List<Bucket>>(list, OK);
    }

    @GetMapping(API_FILE_DELETE)
    public ResponseEntity<Boolean> deleteFile(@RequestParam String bucketName) {

        boolean isTrue = awsS3Service.deleteBucket(bucketName);
        return new ResponseEntity<Boolean>(isTrue, OK);
    }
}
