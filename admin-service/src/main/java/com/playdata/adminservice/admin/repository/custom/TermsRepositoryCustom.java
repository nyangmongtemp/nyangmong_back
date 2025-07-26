package com.playdata.adminservice.admin.repository.custom;

import com.playdata.adminservice.admin.dto.req.SearchDto;
import com.playdata.adminservice.admin.dto.res.TermsDetailResDto;
import com.playdata.adminservice.admin.dto.res.TermsLastPostResDto;
import com.playdata.adminservice.admin.dto.res.TermsListResDto;
import com.playdata.adminservice.admin.entity.TermsCategory;
import com.playdata.adminservice.common.exception.CommonException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TermsRepositoryCustom {

    /**
     * 약관/개인정보처리방침/QNA 목록을 검색 조건 및 페이징 정보에 따라 조회한다.
     *
     * @param termsCategory 조회할 카테고리 (예: TERMS, POLICY, QNA 등)
     * @param searchDto 검색 조건 DTO (제목, 내용, 작성자 등 필터 포함 가능)
     * @param pageable 페이징 및 정렬 정보 (페이지 번호, 크기, 정렬 기준 등)
     * @return 검색 및 페이징 조건에 맞는 약관 목록을 Page 형태로 반환
     */
    Page<TermsListResDto> findByTermsList(TermsCategory termsCategory, SearchDto searchDto, Pageable pageable);

    /**
     * 약관/개인정보처리방침/QNA 상세조회
     *
     * @param id 조회할 약관의 고유 ID
     * @param termsCategory 해당 약관의 카테고리 (TERMS, POLICY, QNA 등)
     * @return 조회된 약관 정보를 담은 TermsDetailResDto 반환
     * @throws CommonException 약관이 존재하지 않으면 DATA_NOT_FOUND 예외 발생
     */
    TermsDetailResDto findByTerms(Long id, TermsCategory termsCategory);

    /**
     * 최신 약관 게시글을 조회한다.
     * 결과가 없을 경우 null을 반환한다.
     *
     * @param termsCategory 조회할 카테고리
     * @return TermsDetailResDto 또는 null
     */
    TermsLastPostResDto findByTermsLastPost(TermsCategory termsCategory);
}
