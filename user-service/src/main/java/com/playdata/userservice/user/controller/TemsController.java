package com.playdata.userservice.user.controller;

import com.playdata.userservice.common.dto.CommonResDto;
import com.playdata.userservice.common.enumeration.ErrorCode;
import com.playdata.userservice.common.exception.CommonException;
import com.playdata.userservice.user.controller.swagger.TermsControllerDocs;
import com.playdata.userservice.user.dto.req.SearchDto;
import com.playdata.userservice.user.dto.res.TermsDetailResDto;
import com.playdata.userservice.user.dto.res.TermsLastPostResDto;
import com.playdata.userservice.user.dto.res.TermsListResDto;
import com.playdata.userservice.user.entity.TermsCategory;
import com.playdata.userservice.user.service.TermsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/terms/{category}")
@RequiredArgsConstructor
@Slf4j
public class TemsController implements TermsControllerDocs {

    private final TermsService termsService;

    /**
     * 약관/개인정보처리방침/QNA 목록조회(검색, 페이징)
     *
     * @param category URL 경로 변수로 전달되는 카테고리명 (예: TERMS, POLICY, QNA)
     * @param page 페이징 정보 (페이지 번호, 사이즈 등)
     * @return 페이징된 약관 목록을 담은 CommonResDto를 ResponseEntity로 반환
     */
    @GetMapping("/list/{page}")
    public ResponseEntity<CommonResDto> getTermsList(@PathVariable String category, @PathVariable(name = "page") int page) {
        TermsCategory termsCategory = parseCategory(category);
        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("createAt")));
        Page<TermsListResDto> result = termsService.findTermsList(termsCategory,pageable);
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
        if (termsCategory != TermsCategory.TERMS && termsCategory != TermsCategory.POLICY) {
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
