package com.playdata.userservice.common.util;

import com.playdata.userservice.common.enumeration.ErrorCode;
import com.playdata.userservice.common.exception.CommonException;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageValidation {

    // 이미지 파일인지 확인하는 유틸리티 메소드
    public static void validateImageFile(MultipartFile file) {

        // 1. MIME 타입 검사
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new CommonException(ErrorCode.MIME_FILE_TYPE);
        }

        // 2. 파일 확장자 검사
        String filename = file.getOriginalFilename();
        if (filename != null && !filename.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif|bmp|webp)$")) {
            throw new CommonException(ErrorCode.MATCHES_FILE_TYPE);
        }
        
        // 아래의 검사 과정까지는 불필요한 듯하여, 추후에 필요할 경우 주석해제
        /*
        // 3. 실제 이미지 디코딩 검사
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new IllegalArgumentException("실제 이미지 파일이 아닙니다. (디코딩 실패)");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 파일을 읽는 중 오류가 발생했습니다.");
        }
        */
    }

}
