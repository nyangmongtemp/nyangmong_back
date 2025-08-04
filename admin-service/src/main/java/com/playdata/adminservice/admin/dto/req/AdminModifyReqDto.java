package com.playdata.adminservice.admin.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "관리자 정보 수정 DTO")
public class AdminModifyReqDto {

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "전화번호", example = "010-1234-1234")
    private String phone;

}
