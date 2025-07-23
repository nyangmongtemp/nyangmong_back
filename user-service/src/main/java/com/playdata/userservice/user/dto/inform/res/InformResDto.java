package com.playdata.userservice.user.dto.inform.res;

import com.playdata.userservice.user.entity.Inform;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InformResDto {

    private Long informId;
    private Long userId;
    private Long adminId;
    private String title;
    private String content;
    private String reply;
    private boolean answered;
    private String nickname;

}
