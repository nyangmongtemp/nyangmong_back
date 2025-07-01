package com.playdata.userservice.common.auth;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenUserInfo {

    private String email;
    private String role;
    private Long userId;
}
