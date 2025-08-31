package com.example.demo.vaildator;

import org.springframework.stereotype.Component;

import com.example.demo.bean.UserBean;
import com.example.demo.dto.ErrorInfo;
import com.example.demo.dto.UpdateUserRequest;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.UserRepository;

@Component
public class EditProfileValidator implements CustValidator<UpdateUserRequest, UserBean> {

    private final UserRepository userRepository;

    public EditProfileValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean support(Class<?> clazz) {
        return UpdateUserRequest.class.equals(clazz);
    }

    @Override
    public UserBean validate(UpdateUserRequest target) {
        ErrorInfo errorInfo = new ErrorInfo();
        UserBean user = userRepository.findById(target.getUserId())
        .orElseThrow(() -> {
            errorInfo.addError("userId", "Connection abnormal");
            return new ApiException("User not found", 404, errorInfo);
        });
        
        // if(target.getFile() == null || target.getFile().isEmpty()) {
        //     return user;
        // }

        // String originalFilename = target.getFile().getOriginalFilename();
        // if (originalFilename == null || originalFilename.isEmpty()) {
        //     errorInfo.addError("file", "File name cannot be empty");
        // }else{
        //     if (originalFilename.length() > 255) {
        //         errorInfo.addError("file", "File name is too long");
        //     }

        //     String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        //     if (!List.of(".jpg", ".jpeg", ".png", ".webp").contains(ext.toLowerCase())) {
        //         errorInfo.addError("file", "File type must be jpg, jpeg, png, or webp");
        //     }
        // }

        
        // if (errorInfo.hasErrors()) {
        //     throw new ApiException("Validation failed", 400, errorInfo);
        // }

        // String uploadDir = System.getProperty("user.dir") + "/upload/";
        // File folder = new File(uploadDir);
        // if (!folder.exists()) folder.mkdirs();

        // String filename = UUID.randomUUID() + "_" + target.getFile().getOriginalFilename();
        // Path path = Paths.get(uploadDir + filename);
        // try {
        //     target.getFile().transferTo(path.toFile());
        // } catch (IOException e) {
        //     errorInfo.addError("file", "File upload failed");
        //     throw new ApiException("File upload failed", 500, errorInfo);
        // }

        
        // target.setImage(filename);
        return user;
    }

}
