package com.playdata.userservice.user.dto.kakao.res;

import com.playdata.userservice.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KakaoLoginResDto {

    private Long userId;

    private String nickname;

    private String email;

    private String profileImage;

    private String token;


}
