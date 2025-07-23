package com.playdata.adminservice.admin.repository.custom;

import com.playdata.adminservice.admin.dto.req.TermsSearchDto;
import com.playdata.adminservice.admin.dto.res.TermsListResDto;
import com.playdata.adminservice.admin.entity.TermsCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TermsRepositoryCustom {

    /**
     * 약관/개인정보처리방침/QNA 목록조회(검색, 페이징)
     *
     * @param searchDto
     * @param pageable
     * @return
     */
    Page<TermsListResDto> findByTermsList(TermsCategory termsCategory, TermsSearchDto searchDto, Pageable pageable);
}
