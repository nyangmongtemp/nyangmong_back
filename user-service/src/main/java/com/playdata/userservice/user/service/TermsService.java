package com.playdata.userservice.user.service;

import com.playdata.userservice.common.auth.TokenUserInfo;
import com.playdata.userservice.common.enumeration.ErrorCode;
import com.playdata.userservice.common.exception.CommonException;
import com.playdata.userservice.user.dto.req.SearchDto;
import com.playdata.userservice.user.dto.req.TermsInsertReqDto;
import com.playdata.userservice.user.dto.req.TermsUpdateReqDto;
import com.playdata.userservice.user.dto.res.TermsDetailResDto;
import com.playdata.userservice.user.dto.res.TermsLastPostResDto;
import com.playdata.userservice.user.dto.res.TermsListResDto;
import com.playdata.userservice.user.entity.Terms;
import com.playdata.userservice.user.entity.TermsCategory;
import com.playdata.userservice.user.repository.TermsRepository;
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
     * @param termsCategory 조회할 카테고리 (TERMS, POLICY, QNA 등)
     * @param pageable 페이징 정보 (페이지 번호, 사이즈, 정렬 등)
     * @return 조건에 맞는 약관 목록을 페이징 처리한 결과(Page)로 반환
     */
    public Page<TermsListResDto> findTermsList(TermsCategory termsCategory, Pageable pageable) {
        return termsRepository.findByTermsList(termsCategory, pageable);
    }

    /**
     * 약관/개인정보처리방침/QNA 상세조회
     *
     * @param id 조회할 약관의 고유 ID
     * @param termsCategory 해당 약관의 카테고리 (TERMS, POLICY, QNA 등)
     * @return 조회된 약관 정보를 담은 TermsDetailResDto 반환
     * @throws CommonException 약관이 존재하지 않으면 DATA_NOT_FOUND 예외 발생
     */
    public TermsDetailResDto termsDetail(Long id, TermsCategory termsCategory) {
        return termsRepository.findByTerms(id, termsCategory);
    }


    /**
     * 가장 최근 약관 게시글을 반환한다.
     *
     * 조건:
     * - 비활성화된 약관은 제외 (active = true)
     * - 게시글이 존재하지 않을 경우 null 반환
     *
     * @param category 조회할 약관 카테고리
     * @return 가장 최근의 TermsDetailResDto 또는 null
     */
    public TermsLastPostResDto getLastPostTerms(TermsCategory category) {
        return termsRepository.findByTermsLastPost(category);
    }

    /**
     * 약관 ID와 카테고리, 활성 상태가 true인 약관을 조회한다.
     * 조건에 맞는 약관이 없으면 CommonException(DATA_NOT_FOUND) 예외를 던진다.
     *
     * @param id 조회할 약관 ID
     * @param category 조회할 약관 카테고리
     * @return 조회된 Terms 엔티티
     * @throws CommonException 약관이 없으면 발생
     */
    private Terms findTermsOrThrow(Long id, TermsCategory category) {
        return termsRepository.findByTermsIdAndCategoryAndActiveIsTrue(id, category)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));
    }


}
