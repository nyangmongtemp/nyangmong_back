package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.TermsInsertReqDto;
import com.playdata.adminservice.admin.dto.req.SearchDto;
import com.playdata.adminservice.admin.dto.req.TermsUpdateReqDto;
import com.playdata.adminservice.admin.dto.res.TermsDetailResDto;
import com.playdata.adminservice.admin.dto.res.TermsLastPostResDto;
import com.playdata.adminservice.admin.dto.res.TermsListResDto;
import com.playdata.adminservice.admin.entity.Terms;
import com.playdata.adminservice.admin.entity.TermsCategory;
import com.playdata.adminservice.admin.service.TermsService;
import com.playdata.adminservice.common.auth.TokenUserInfo;
import com.playdata.adminservice.common.dto.CommonResDto;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import com.playdata.adminservice.common.exception.CommonException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/{category}")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'CUSTOMER')")
public class TermsController {

    private final TermsService termsService;

    /**
     * 약관/개인정보처리방침/QNA 목록조회(검색, 페이징)
     *
     * @param category URL 경로 변수로 전달되는 카테고리명 (예: TERMS, POLICY, QNA)
     * @param searchDto 검색 조건이 담긴 DTO (검색어 등)
     * @param pageable 페이징 정보 (페이지 번호, 사이즈 등)
     * @return 페이징된 약관 목록을 담은 CommonResDto를 ResponseEntity로 반환
     */
    @GetMapping("/list")
    public ResponseEntity<CommonResDto> getTermsList(@PathVariable String category, SearchDto searchDto, Pageable pageable) {
        TermsCategory termsCategory = parseCategory(category);
        Page<TermsListResDto> result = termsService.findTermsList(termsCategory, searchDto, pageable);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "목록 조회", result);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * 약관/개인정보처리방침/QNA 상세조회
     *
     * @param category URL 경로 변수로 전달되는 카테고리명
     * @param id 상세 조회할 약관 ID
     * @return 약관 상세 정보를 담은 CommonResDto를 ResponseEntity로 반환
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommonResDto> getTerms(@PathVariable String category, @PathVariable Long id) {
        TermsCategory termsCategory = parseCategory(category);
        TermsDetailResDto result = termsService.termsDetail(id, termsCategory);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "상세 조회", result);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * 약관/개인정보처리방침/QNA 등록
     *
     * @param adminInfo 인증된 관리자 정보 (Spring Security AuthenticationPrincipal)
     * @param category URL 경로 변수로 전달되는 카테고리명
     * @param termsInsertReqDto 등록할 약관 정보가 담긴 요청 DTO
     * @return 등록된 약관 엔티티를 담은 CommonResDto를 ResponseEntity로 반환
     */
    @PostMapping()
    public ResponseEntity<CommonResDto> createTerms(
            @AuthenticationPrincipal TokenUserInfo adminInfo,
            @PathVariable String category,
            @RequestBody @Valid TermsInsertReqDto termsInsertReqDto) {
        TermsCategory termsCategory = parseCategory(category);
        Terms result = termsService.insertTerms(adminInfo, termsCategory, termsInsertReqDto);
        CommonResDto resDto = new CommonResDto(HttpStatus.CREATED, "등록 완료", result);
        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }

    /**
     * 약관/개인정보처리방침/QNA 수정
     *
     * @param id 수정할 약관 ID
     * @param category URL 경로 변수로 전달되는 카테고리명
     * @param termsUpdateReqDto 수정할 내용이 담긴 요청 DTO
     * @return 수정 완료된 약관 엔티티를 담은 CommonResDto를 ResponseEntity로 반환
     */
    @PatchMapping("/{id}")
    public ResponseEntity<CommonResDto> updateTerms(
            @AuthenticationPrincipal TokenUserInfo adminInfo,
            @PathVariable Long id,
            @PathVariable String category,
            @RequestBody @Valid TermsUpdateReqDto termsUpdateReqDto) {
        TermsCategory termsCategory = parseCategory(category);
        Terms result = termsService.updateTerms(adminInfo, id, termsCategory, termsUpdateReqDto);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "수정 완료", result);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * 약관/개인정보처리방침/QNA 삭제
     *
     * @param id 삭제할 약관 ID
     * @param category URL 경로 변수로 전달되는 카테고리명
     * @return 삭제 완료된 약관 엔티티를 담은 CommonResDto를 ResponseEntity로 반환
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResDto> deleteTerms(@PathVariable Long id, @PathVariable String category) {
        TermsCategory termsCategory = parseCategory(category);
        Terms result = termsService.deleteTerms(id, termsCategory);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "삭제 완료", result);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * 가장 최근 약관 게시글을 조회하는 API
     *
     * 조건:
     * - 카테고리는 반드시 'TERMS' 이어야 한다. (기타 카테고리는 BAD_REQUEST 예외 발생)
     * - 최근 게시글이 없을 경우, data=null로 응답된다.
     *
     * @param category 문자열 형태의 카테고리명 (ex: "TERMS")
     * @return CommonResDto (status, message, data 포함)
     */
    @GetMapping("/lastPost")
    public ResponseEntity<CommonResDto> getLastPostTerms(@PathVariable String category) {
        TermsCategory termsCategory = parseCategory(category);
        if (termsCategory != TermsCategory.TERMS) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        TermsLastPostResDto result = termsService.getLastPostTerms(termsCategory);
        String message = (result == null) ? "등록된 약관이 없습니다." : "약관 마지막 게시글 조회";
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, message, result);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * URL 경로 변수로 들어온 문자열 category를 TermsCategory Enum으로 변환한다.
     * 변환에 실패하면 BAD_REQUEST 예외를 발생시킨다.
     *
     * @param category 문자열 카테고리 (예: "TERMS", "POLICY", "QNA")
     * @return 변환된 TermsCategory Enum
     * @throws CommonException 변환 실패 시 발생 (BAD_REQUEST)
     */
    private TermsCategory parseCategory(String category) {
        try {
            return TermsCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
    }
}