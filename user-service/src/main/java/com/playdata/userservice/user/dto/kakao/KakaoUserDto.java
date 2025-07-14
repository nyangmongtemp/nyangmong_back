package com.playdata.userservice.user.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.playdata.userservice.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoUserDto {

    private Long id;

    @JsonProperty("connected_at")
    private LocalDateTime connectedAt;

    private Properties properties;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @Setter
    @ToString
    public static class Properties {
        private String nickname;

        @JsonProperty("profile_image")
        private String profileImage;

        @JsonProperty("thumbnail_image")
        private String thumbnailImage;
    }

    @Getter
    @Setter
    @ToString
    public static class KakaoAccount {
        private String email;
    }

    public User makeNewKakaoUser() {
        String profile = null;
        if(this.properties.profileImage != null) {
            profile = this.properties.profileImage;
        }
        else {
            profile ="default_user.png";
        }
        return User.builder()
                .userName(this.properties.nickname)
                .nickname(this.properties.nickname)
                .profileImage(profile)
                .email(this.kakaoAccount.email)
                .socialId(this.id.toString())
                .socialProvider("KAKAO")
                .password("KAKAO")
                .active(true)
                .grade(0L)
                .reportCount(0)
                .passwordFaultCount(0)
                .pauseCount(0)
                .passwordUpdatedAt(LocalDateTime.now())
                .address(null)
                .phone(null)
                .build();
    }

}