package com.niovoglee.springboots3fileupload.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AWSS3UploadService implements FileUploadService {

    @Value(value = "${niovo.cloud.aws.bucket.name}")
    String bucketName;

    @Autowired
    private AmazonS3 amazonS3;

    @Override
    public String uploadFile(MultipartFile file) {

        String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        String key = String.format("%s.%s", UUID.randomUUID(), filenameExtension);

        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(file.getSize());
        metaData.setContentType(file.getContentType());

        try {
            if (!amazonS3.doesBucketExist(bucketName)) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                                  "Bucket name is not available. Try again with a different Bucket name.");
            }
            amazonS3.putObject(bucketName, key, file.getInputStream(), metaData);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An exception occured while uploading the file");
        }

        amazonS3.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);

        return getResourceUrl(bucketName, key);

    }

    @Override
    public List<Bucket> listBucket() {

        return amazonS3.listBuckets();
    }

    @Override
    public boolean deleteBucket(String bucketName) {

        try {
            amazonS3.deleteBucket(bucketName);
            return true;
        } catch (AmazonServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getErrorMessage());
        }
    }

    public String getResourceUrl(String bucketName, String key) {

        try {
            return amazonS3.getUrl(bucketName, key).toString();
        } catch (Exception var4) {
            return null;
        }
    }
}
