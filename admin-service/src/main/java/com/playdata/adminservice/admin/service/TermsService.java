package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.TermsInsertReqDto;
import com.playdata.adminservice.admin.dto.req.TermsSearchDto;
import com.playdata.adminservice.admin.dto.req.TermsUpdateReqDto;
import com.playdata.adminservice.admin.dto.res.TermsDetailResDto;
import com.playdata.adminservice.admin.dto.res.TermsListResDto;
import com.playdata.adminservice.admin.entity.Terms;
import com.playdata.adminservice.admin.entity.TermsCategory;
import com.playdata.adminservice.admin.repository.TermsRepository;
import com.playdata.adminservice.common.auth.TokenUserInfo;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import com.playdata.adminservice.common.exception.CommonException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@Slf4j
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;

    /**
     * 약관/개인정보처리방침/QNA 목록조회(검색, 페이징)
     *
     * @param category
     * @param searchDto
     * @param pageable
     * @return
     */
    public Page<TermsListResDto> findTermsList(String category, TermsSearchDto searchDto, Pageable pageable) {
        parseCategory(category);
        return termsRepository.findByTermsList(searchDto, pageable);
    }

    /**
     * 약관/개인정보처리방침/QNA 상세조회
     *
     * @param category
     * @param id
     * @return
     */
    public TermsDetailResDto termsDetail(String category, Long id) {
        parseCategory(category);
        return new TermsDetailResDto(findTermsOrThrow(id));
    }

    /**
     * 약관/개인정보처리방침/QNA 등록
     *
     * @param userInfo
     * @param termsInsertReqDto
     * @return
     */
    @Transactional
    public Terms insertTerms(TokenUserInfo userInfo, @PathVariable String category, @Valid TermsInsertReqDto termsInsertReqDto) {
        TermsCategory termsCategory = parseCategory(category);
        Long adminId = userInfo.getAdminId();
        return termsRepository.save(termsInsertReqDto.toEntity(adminId, termsCategory));
    }

    /**
     * 약관/개인정보처리방침/QNA 수정
     *
     * @param id
     * @param termsUpdateReqDto
     * @return
     */
    @Transactional
    public Terms updateTerms(@PathVariable Long id, @PathVariable String category, @Valid TermsUpdateReqDto termsUpdateReqDto) {
        parseCategory(category);
        Terms terms = findTermsOrThrow(id);
        terms.updateTerms(termsUpdateReqDto);
        return terms;
    }

    /**
     * 약관/개인정보처리방침/QNA 삭제
     *
     * @param id
     * @return
     */
    @Transactional
    public Terms deleteTerms(Long id, @PathVariable String category) {
        parseCategory(category);
        Terms terms = findTermsOrThrow(id);
        terms.deleteTerms();
        return terms;
    }

    /**
     * 게시물이 실제로 존재하는지 확인
     *
     * @param id
     * @return
     */
    private Terms findTermsOrThrow(Long id) {
        return termsRepository.findByTermsIdAndActiveIsTrue(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));
    }

    /**
     * 주소로 들어온 값 Eunm 비교
     * @param category
     * @return
     */
    private TermsCategory parseCategory(String category) {
        try {
            return TermsCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
    }
}
