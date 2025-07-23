package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.TermsInsertReqDto;
import com.playdata.adminservice.admin.dto.req.TermsUpdateReqDto;
import com.playdata.adminservice.admin.entity.Terms;
import com.playdata.adminservice.admin.service.TermsService;
import com.playdata.adminservice.common.auth.TokenUserInfo;
import com.playdata.adminservice.common.dto.CommonResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/terms/{category}")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'CUSTOMER')")
public class TermsController {

    private final TermsService termsService;

    /**
     * 약관/개인정보처리방침/QNA 등록
     *
     * @param userInfo
     * @param termsInsertReqDto
     * @return
     */
    @PostMapping()
    public ResponseEntity<CommonResDto> createTerms(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PathVariable String category,
            @RequestBody @Valid TermsInsertReqDto termsInsertReqDto) {
        Terms result = termsService.insertTerms(userInfo, category, termsInsertReqDto);
        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "등록 완료", result);
        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    /**
     * 약관/개인정보처리방침/QNA 수정
     *
     * @param id
     * @param termsUpdateReqDto
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommonResDto> updateTerms(
            @PathVariable Long id,
            @PathVariable String category,
            @RequestBody @Valid TermsUpdateReqDto termsUpdateReqDto) {
        Terms result = termsService.updateTerms(id, category, termsUpdateReqDto);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "수정 완료", result);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * 약관/개인정보처리방침/QNA 삭제
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResDto> deleteTerms(@PathVariable Long id, @PathVariable String category) {
        Terms result = termsService.deleteTerms(id, category);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "삭제 완료", result);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

}
