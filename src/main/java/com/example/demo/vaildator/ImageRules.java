package com.example.demo.vaildator;

import java.io.IOException;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ErrorInfo;
import com.example.demo.exception.ApiException;

@Component
public class ImageRules {
    private static final long MAX_SIZE = 2 * 1024 * 1024; // 2MB
    private static final Set<String> ALLOWED_EXT = Set.of(".jpg", ".jpeg", ".png", ".webp");
    private static final Set<String> ALLOWED_MIME = Set.of("image/jpeg","image/png","image/webp");

    public void check(MultipartFile file) {
        ErrorInfo ei = new ErrorInfo();

        if (file == null || file.isEmpty()) {
            ei.addError("file", "File cannot be empty");
            throw new ApiException("Validation failed", 400, ei);
        }

        String name = file.getOriginalFilename();
        if (name == null || name.isBlank()) {
            ei.addError("file", "File name cannot be empty");
        } else if (name.length() > 255) {
            ei.addError("file", "File name is too long");
        }

        String ext = "";
        if (name != null) {
            int dot = name.lastIndexOf('.');
            if (dot >= 0) ext = name.substring(dot).toLowerCase();
        }
        if (!ALLOWED_EXT.contains(ext)) {
            ei.addError("file", "File type must be jpg, jpeg, png, or webp");
        }

        if (file.getSize() > MAX_SIZE) {
            ei.addError("file", "File too large (max 2MB)");
        }
        if (file.getContentType() == null || !ALLOWED_MIME.contains(file.getContentType())) {
            ei.addError("file", "Unsupported content type");
        }

        try (var is = file.getInputStream()) {
            if (javax.imageio.ImageIO.read(is) == null) {
                ei.addError("file", "Invalid image content");
            }
        } catch (IOException e) {
            ei.addError("file", "Cannot read file");
        }

        if (ei.hasErrors()) throw new ApiException("Validation failed", 400, ei);
    }
}
