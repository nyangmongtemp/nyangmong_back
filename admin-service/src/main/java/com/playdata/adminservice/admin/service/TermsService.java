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
     * @param termsCategory 조회할 카테고리 (TERMS, POLICY, QNA 등)
     * @param searchDto 검색 조건(검색어 등)
     * @param pageable 페이징 정보 (페이지 번호, 사이즈, 정렬 등)
     * @return 조건에 맞는 약관 목록을 페이징 처리한 결과(Page)로 반환
     */
    public Page<TermsListResDto> findTermsList(TermsCategory termsCategory, TermsSearchDto searchDto, Pageable pageable) {
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
        return new TermsDetailResDto(findTermsOrThrow(id, termsCategory));
    }

    /**
     * 약관/개인정보처리방침/QNA 등록
     *
     * @param userInfo 현재 인증된 관리자 정보 (adminId 포함)
     * @param category 등록할 약관 카테고리 (TERMS, POLICY, QNA 등)
     * @param termsInsertReqDto 약관 등록을 위한 요청 DTO
     * @return 저장된 Terms 엔티티 반환
     */
    @Transactional
    public Terms insertTerms(TokenUserInfo userInfo, TermsCategory category, TermsInsertReqDto termsInsertReqDto) {
        Long adminId = userInfo.getAdminId();
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
    public Terms updateTerms(Long id, TermsCategory termsCategory, TermsUpdateReqDto termsUpdateReqDto) {
        Terms terms = findTermsOrThrow(id, termsCategory);
        terms.updateTerms(termsUpdateReqDto);
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
     * 특정 카테고리에 해당하며 활성 상태가 true인 약관 중
     * 가장 최근에 등록된 약관 한 건을 조회한다.
     *
     * @param category 조회할 TermsCategory (TERMS, POLICY, QNA)
     * @return 조건에 맞는 최신 약관을 Optional로 감싸 반환, 없으면 Optional.empty()
     */
    public Optional<TermsDetailResDto> getLastPostTerms(TermsCategory category) {
        Optional<Terms> terms = termsRepository.findTopByCategoryAndActiveIsTrueOrderByTermsIdDesc(category);
        return terms.map(TermsDetailResDto::new);
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
