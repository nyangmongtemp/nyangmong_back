package com.playdata.userservice.user.dto.inform.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "문의 수정 요청 DTO")
public class InformModiReqDto {

    @NotNull
    @Schema(description = "수정할 문의 id", example = "1")
    private Long informId;
    
    // 제목, 내용 둘중 하나만 수정할 수도 있기 때문에, NotBlank를 쓰지 않음
    // 수정하는 메소드 단에서 둘 다 null인 경우만 에러로 처리할 예정
    @Schema(description = "수정할 문의 제목", example = "문의 수정 예시 제목")
    private String title;

    @Schema(description = "수정할 문의 내용", example = "문의 수정 예시 내용")
    private String content;

}
