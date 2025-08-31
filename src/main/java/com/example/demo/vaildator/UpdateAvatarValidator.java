package com.example.demo.vaildator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.demo.bean.UserBean;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.UpdateUserAvatarRequqst;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.UserRepository;

@Component
public class UpdateAvatarValidator implements CustValidator<UpdateUserAvatarRequqst, UserBean> {

    private final UserRepository userRepository;

    public UpdateAvatarValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean support(Class<?> clazz) {
        return UpdateUserAvatarRequqst.class.equals(clazz);
    }

    @Override
    public UserBean validate(UpdateUserAvatarRequqst target) {
        ErrorInfo errorInfo = new ErrorInfo();
        UserBean user = userRepository.findById(target.getUserId())
        .orElseThrow(() -> {
            errorInfo.addError("userId", "Connection abnormal");
            return new ApiException("User not found", 404, errorInfo);
        });
        
        if(target.getFile() == null || target.getFile().isEmpty()) {
            return user;
        }

        String originalFilename = target.getFile().getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            errorInfo.addError("file", "File name cannot be empty");
        }else{
            if (originalFilename.length() > 255) {
                errorInfo.addError("file", "File name is too long");
            }

            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (!List.of(".jpg", ".jpeg", ".png", ".webp").contains(ext.toLowerCase())) {
                errorInfo.addError("file", "File type must be jpg, jpeg, png, or webp");
            }
        }

        
        if (errorInfo.hasErrors()) {
            throw new ApiException("Validation failed", 400, errorInfo);
        }

        String uploadDir = System.getProperty("user.dir") + "/upload/";
        File folder = new File(uploadDir);
        if (!folder.exists()) folder.mkdirs();

        String filename = UUID.randomUUID() + "_" + target.getFile().getOriginalFilename();
        Path path = Paths.get(uploadDir + filename);
        try {
            target.getFile().transferTo(path.toFile());
        } catch (IOException e) {
            errorInfo.addError("file", "File upload failed");
            throw new ApiException("File upload failed", 500, errorInfo);
        }

        user.setImage("/upload/" + filename);
        return user;
    }

}
