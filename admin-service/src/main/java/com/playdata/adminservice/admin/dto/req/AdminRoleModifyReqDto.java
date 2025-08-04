package com.playdata.adminservice.admin.dto.req;

import com.playdata.adminservice.admin.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "관리자 권한/활성화 상태 변경 DTO")
public class AdminRoleModifyReqDto {

    @NotBlank
    @Schema(description = "관리자 번호", example = "1")
    private Long adminId;

    @NotNull
    @Schema(description = "권한상태", example = "BOSS")
    private Role role;

    @NotNull
    @Schema(description = "활성화 상태", example = "true")
    private Boolean active;

}
