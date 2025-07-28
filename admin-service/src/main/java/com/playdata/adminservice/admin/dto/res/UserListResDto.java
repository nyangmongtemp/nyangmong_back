package com.playdata.adminservice.admin.dto.res;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserListResDto {

    private Long userId;
    private String userName;
    private String email;
    private String nickname;
    private boolean active;;
    private int pauseCount;
    private LocalDateTime createAt;

    private int reportCount;

}
