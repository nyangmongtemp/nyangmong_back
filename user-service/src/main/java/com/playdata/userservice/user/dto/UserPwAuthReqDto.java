package com.playdata.userservice.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.aspectj.apache.bcel.classfile.Code;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPwAuthReqDto {

    @NotBlank
    String authCode;

}
