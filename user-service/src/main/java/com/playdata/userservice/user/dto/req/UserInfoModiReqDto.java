package com.playdata.userservice.user.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserInfoModiReqDto {

    String nickname;

    @NotNull
    String phone;

    @NotNull
    String address;

}
