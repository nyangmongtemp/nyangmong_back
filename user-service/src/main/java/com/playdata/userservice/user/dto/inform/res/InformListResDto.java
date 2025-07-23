package com.playdata.userservice.user.dto.inform.res;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InformListResDto {

    private Long informId;
    private Long userId;
    private String title;
    private boolean answered;
    // 생성 시각은 필요 없을 수도
    private LocalDateTime createAt;
    // 답변이 생성되면 updateAt이 바뀌기 때문에 필요
    private LocalDateTime updateAt;
    // 이건 사실 필요 없을지도
    private String nickname;
    
}
