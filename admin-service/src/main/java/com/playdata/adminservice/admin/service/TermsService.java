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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;

    /**
     * 약관/개인정보처리방침/QNA 목록조회(검색, 페이징)
     *
     * @param searchDto
     * @param pageable
     * @return
     */
    public Page<TermsListResDto> findTermsList(TermsCategory termsCategory, TermsSearchDto searchDto, Pageable pageable) {
        return termsRepository.findByTermsList(termsCategory, searchDto, pageable);
    }

    /**
     * 약관/개인정보처리방침/QNA 상세조회
     *
     * @param id
     * @return
     */
    public TermsDetailResDto termsDetail(Long id, TermsCategory termsCategory) {
        return new TermsDetailResDto(findTermsOrThrow(id, termsCategory));
    }

    /**
     * 약관/개인정보처리방침/QNA 등록
     *
     * @param userInfo
     * @param termsInsertReqDto
     * @return
     */
    @Transactional
    public Terms insertTerms(TokenUserInfo userInfo, TermsCategory category, TermsInsertReqDto termsInsertReqDto) {
        Long adminId = userInfo.getAdminId();
        return termsRepository.save(termsInsertReqDto.toEntity(adminId, category));
    }

    /**
     * 약관/개인정보처리방침/QNA 수정
     *
     * @param id
     * @param termsUpdateReqDto
     * @return
     */
    @Transactional
    public Terms updateTerms(Long id, TermsCategory termsCategory, TermsUpdateReqDto termsUpdateReqDto) {
        Terms terms = findTermsOrThrow(id, termsCategory);
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
    public Terms deleteTerms(Long id, TermsCategory termsCategory) {
        Terms terms = findTermsOrThrow(id, termsCategory);
        terms.deleteTerms();
        return terms;
    }

    /**
     * 약관 마지막게시글 조회
     *
     * @param category
     * @return
     */
    public Optional<TermsDetailResDto> getLastPostTerms(TermsCategory category) {
        Optional<Terms> terms = termsRepository.findTopByCategoryAndActiveIsTrueOrderByTermsIdDesc(category);
        return terms.map(TermsDetailResDto::new);
    }

    /**
     * 게시물이 실제로 존재하는지 확인
     *
     * @param id
     * @return
     */
    private Terms findTermsOrThrow(Long id, TermsCategory category) {
        return termsRepository.findByTermsIdAndCategoryAndActiveIsTrue(id, category)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));
    }
}
