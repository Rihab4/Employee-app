package com.codewithrihab.employeeapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
public class S3StorageService implements StorageService {

    private final S3Client s3Client;
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3StorageService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String uploadEmployeePhoto(MultipartFile file) {
        try {
            String key = "employees/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );

            return "https://" + bucketName + ".s3.us-east-1.amazonaws.com/" + key;

        } catch (Exception e) {
            try {
                throw new FileUploadException("Error uploading employee photo", e);
            } catch (FileUploadException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
