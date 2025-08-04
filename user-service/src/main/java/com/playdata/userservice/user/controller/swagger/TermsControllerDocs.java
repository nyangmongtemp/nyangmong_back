package com.playdata.userservice.user.controller.swagger;

import com.playdata.userservice.common.dto.CommonResDto;
import com.playdata.userservice.common.enumeration.TermsSwaggerEx;
import com.playdata.userservice.common.enumeration.UserSwaggerEx;
import com.playdata.userservice.common.exception.CommonException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "고객센터 서비스", description = "문의, 개인정보처리방침, 이용약관 조회 API")
public interface TermsControllerDocs {

    @Operation(summary = "약관/개인정보처리방침/QNA 목록조회",
            description = """
               약관, 개인정보처리방침 Q&A 목록 조회

           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = TermsSwaggerEx.TERMS_LIST)
            )),
            @ApiResponse(
                    responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = TermsSwaggerEx.BAD_REQUEST)
            ))
    })
    ResponseEntity<CommonResDto> getTermsList(@PathVariable String category, @PathVariable(name = "page") int page);

    
    @Operation(summary = "약관/개인정보처리방침/QNA 상세조회",
            description = """
               약관/개인정보처리방침/QNA 상세조회

           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = TermsSwaggerEx.TERMS_DETAIL)
            )),
            @ApiResponse(
                    responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = TermsSwaggerEx.BAD_REQUEST)
            ))
    })
    ResponseEntity<CommonResDto> getTerms(@PathVariable String category, @PathVariable Long id);

    
    @Operation(summary = "약관/개인정보처리방침 최신 글 상세조회",
            description = """
               약관/개인정보처리방침 최신글 상세조회

           """)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = TermsSwaggerEx.LAST_POST)
            )),
            @ApiResponse(
                    responseCode = "400", description = "조회 실패", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CommonException.class),
                    examples = @ExampleObject(value = TermsSwaggerEx.BAD_REQUEST)
            ))
    })
    ResponseEntity<CommonResDto> getLastPostTerms(@PathVariable String category);

}
