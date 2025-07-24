package com.playdata.adminservice.admin.repository.custom;

import com.playdata.adminservice.admin.dto.req.SearchDto;
import com.playdata.adminservice.admin.dto.res.InformDetailResDto;
import com.playdata.adminservice.admin.dto.res.InformListResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InformRepositoryCustom {

    /**
     * 검색조건과 페이징에 따른 문의 리스트 조회
     *
     * @param searchDto 검색 조건 DTO
     * @param pageable 페이징 정보
     * @return 문의 리스트 페이지
     */
    Page<InformListResDto> findByInformList(SearchDto searchDto, Pageable pageable);

    /**
     * 문의 상세 조회
     *
     * @param id 문의 ID
     * @return 상세 DTO
     */
    InformDetailResDto findByInform(Long id);
}
