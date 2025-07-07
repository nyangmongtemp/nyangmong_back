package com.playdata.userservice.user.dto;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class UserInfoModiReqDto {

    String nickname;

    String phone;

    String address;

}
