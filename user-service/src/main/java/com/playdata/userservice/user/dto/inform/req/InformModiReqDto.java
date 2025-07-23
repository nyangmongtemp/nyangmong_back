package com.playdata.userservice.user.dto.inform.req;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InformModiReqDto {

    @NotNull
    private Long informId;
    // 제목, 내용 둘중 하나만 수정할 수도 있기 때문에, NotBlank를 쓰지 않음
    // 수정하는 메소드 단에서 둘 다 null인 경우만 에러로 처리할 예정
    private String title;
    private String content;

}
