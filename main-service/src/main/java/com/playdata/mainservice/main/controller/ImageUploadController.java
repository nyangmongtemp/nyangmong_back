package com.playdata.mainservice.main.controller;

import com.playdata.mainservice.common.configs.AwsS3Config;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/editor")
@RequiredArgsConstructor
@Hidden
public class ImageUploadController {

    // TODO: 실제 s3 주소가 들어가게되면 yml 에 해상 주소 올려서 변경필요
//    @Value("${upload.path:/Users/ubing/Desktop/nyangmong/images/editor}")
    @Value("${upload.path:C:\\nyangmong_image/images/editor}")
    private String uploadPath;

    private final AwsS3Config s3Config;

    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, Object>> uploadImage(
            @RequestParam("upload") MultipartFile file,
            @RequestParam("boardType") String boardType) {

        try {
            String profileImagePath = null;
            // 파일 검증
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("uploaded", false, "error", "파일이 비어있습니다."));
            }

            // 허용된 이미지 타입 체크
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("uploaded", false, "error", "이미지 파일만 업로드 가능합니다."));
            }

            // 파일명 생성 (중복 방지)
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String fileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + extension;

            // s3 버킷에 이미지 저장하고 저장된 경로를 받아오기
            profileImagePath = s3Config.uploadToS3Bucket(file.getBytes(), "editor/"+boardType+"/"+fileName);

//            // 업로드 경로 생성
//            String uploadDir = uploadPath + "/" + boardType + "/";
//            File dir = new File(uploadDir);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
//
//            // 파일 저장
//            Path filePath = Paths.get(uploadDir + filename);
//            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

//            // TODO: 실제 s3 주소가 들어가게되면 실제 불러올 url 주소 변경 필요
//            String imageUrl = "http://localhost:8000/images/editor/" + boardType + "/" + filename;

            return ResponseEntity.ok(Map.of(
                    "uploaded", true,
                    "url", profileImagePath
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("uploaded", false, "error", "업로드 실패: " + e.getMessage()));
        }
    }
}