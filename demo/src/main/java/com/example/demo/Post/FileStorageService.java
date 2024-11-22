package com.example.demo.Post;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${upload.path}")  // application.properties에서 설정한 경로
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {
        // 파일 이름 생성 (UUID 사용)
        String fileName = UUID.randomUUID().toString() + "." + getFileExtension(file.getOriginalFilename());
        Path targetLocation = Paths.get(uploadDir, fileName);

        // 디렉토리가 없으면 생성
        File targetDir = new File(uploadDir);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }

        // 파일 저장
        file.transferTo(targetLocation);

        // 저장된 파일 경로 반환 (웹에서 접근할 수 있는 경로)
        return "/uploads/" + fileName;
    }

    private String getFileExtension(String filename) {
        int index = filename.lastIndexOf('.');
        return (index > 0) ? filename.substring(index + 1) : "";
    }
}

