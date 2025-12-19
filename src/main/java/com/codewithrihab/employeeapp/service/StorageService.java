package com.codewithrihab.employeeapp.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String uploadEmployeePhoto(MultipartFile file);
}
