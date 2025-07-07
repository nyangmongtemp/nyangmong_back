package com.playdata.userservice.user.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class UserInfoModiReqDto {

    String nickname;

    @NotNull
    String phone;

    @NotNull
    String address;

}
