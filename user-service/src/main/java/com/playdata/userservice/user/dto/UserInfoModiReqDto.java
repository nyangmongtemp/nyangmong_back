package com.playdata.userservice.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoModiReqDto {

    String nickname;

    MultipartFile profileImage;

    String phone;

    String address;

}
