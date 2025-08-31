package com.example.demo.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ErrorInfo;
import com.example.demo.exception.ApiException;

@Service
public class FileStorageService {

    /**
     * Stores the uploaded file to the local 'upload' directory.
     *
     * @param file the MultipartFile to be stored
     * @return the relative path to the stored file
     * @throws ApiException if file upload fails
     */
    public String storeFile(MultipartFile file) {
        Path uploadDirPath = Paths.get(System.getProperty("user.dir"), "upload");
        File folder = uploadDirPath.toFile();
        if (!folder.exists()) folder.mkdirs();

        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
        String filename = UUID.randomUUID() + "_" + originalFilename;
        Path path = Paths.get(uploadDirPath.toString(), filename);
        try {
            file.transferTo(path.toFile());
        } catch (IOException e) {
            ErrorInfo errorInfo = new ErrorInfo();
            errorInfo.addError("file", "File upload failed");
            throw new ApiException("File upload failed", 400, errorInfo);
        }

        return "/upload/" + filename;
    }
    
}
