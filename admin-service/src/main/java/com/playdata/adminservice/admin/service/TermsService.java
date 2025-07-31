package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.TermsInsertReqDto;
import com.playdata.adminservice.admin.dto.req.SearchDto;
import com.playdata.adminservice.admin.dto.req.TermsUpdateReqDto;
import com.playdata.adminservice.admin.dto.res.TermsDetailResDto;
import com.playdata.adminservice.admin.dto.res.TermsLastPostResDto;
import com.playdata.adminservice.admin.dto.res.TermsListResDto;
import com.playdata.adminservice.admin.entity.Terms;
import com.playdata.adminservice.admin.entity.TermsCategory;
import com.playdata.adminservice.admin.repository.TermsRepository;
import com.playdata.adminservice.common.auth.TokenAdminInfo;
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
     * @param termsCategory 조회할 카테고리 (TERMS, POLICY, QNA 등)
     * @param searchDto 검색 조건(검색어 등)
     * @param pageable 페이징 정보 (페이지 번호, 사이즈, 정렬 등)
     * @return 조건에 맞는 약관 목록을 페이징 처리한 결과(Page)로 반환
     */
    public Page<TermsListResDto> findTermsList(TermsCategory termsCategory, SearchDto searchDto, Pageable pageable) {
        return termsRepository.findByTermsList(termsCategory, searchDto, pageable);
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
        return Optional.ofNullable(termsRepository.findByTerms(id, termsCategory)).orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));
    }

    /**
     * 약관/개인정보처리방침/QNA 등록
     *
     * @param adminInfo 현재 인증된 관리자 정보 (adminId 포함)
     * @param category 등록할 약관 카테고리 (TERMS, POLICY, QNA 등)
     * @param termsInsertReqDto 약관 등록을 위한 요청 DTO
     * @return 저장된 Terms 엔티티 반환
     */
    @Transactional
    public Terms insertTerms(TokenAdminInfo adminInfo, TermsCategory category, TermsInsertReqDto termsInsertReqDto) {
        Long adminId = adminInfo.getAdminId();
        return termsRepository.save(termsInsertReqDto.toEntity(adminId, category));
    }

    /**
     * 약관/개인정보처리방침/QNA 수정
     *
     * @param id 수정할 약관의 고유 ID
     * @param termsCategory 수정 대상 약관의 카테고리
     * @param termsUpdateReqDto 수정할 내용을 담은 DTO
     * @return 수정된 Terms 엔티티 반환
     * @throws CommonException 해당 약관이 없으면 DATA_NOT_FOUND 예외 발생
     */
    @Transactional
    public Terms updateTerms(TokenAdminInfo adminInfo, Long id, TermsCategory termsCategory, TermsUpdateReqDto termsUpdateReqDto) {
        Terms terms = findTermsOrThrow(id, termsCategory);
        terms.updateTerms(adminInfo.getAdminId(), termsUpdateReqDto);
        return terms;
    }

    /**
     * 약관/개인정보처리방침/QNA 삭제 (활성 상태 false 처리)
     *
     * @param id 삭제할 약관의 고유 ID
     * @param termsCategory 삭제 대상 약관의 카테고리
     * @return 삭제 처리된 Terms 엔티티 반환
     * @throws CommonException 해당 약관이 없으면 DATA_NOT_FOUND 예외 발생
     */
    @Transactional
    public Terms deleteTerms(Long id, TermsCategory termsCategory) {
        Terms terms = findTermsOrThrow(id, termsCategory);
        terms.deleteTerms(); // 활성 상태 false 처리 등 삭제 로직
        return terms;
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
