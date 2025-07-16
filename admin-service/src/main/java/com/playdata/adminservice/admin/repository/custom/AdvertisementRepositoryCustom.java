package com.playdata.adminservice.admin.repository.custom;

import com.playdata.adminservice.admin.dto.req.AdSearchDto;
import com.playdata.adminservice.admin.dto.res.AdResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 광고 검색을 위한 사용자 정의 레포지토리 인터페이스
 * (QueryDSL 등을 사용하여 동적 검색 쿼리를 정의하기 위함)
 */
public interface AdvertisementRepositoryCustom {

    /**
     * 광고 목록 검색 (조건 + 페이징)
     *
     * @param searchDto 검색 조건 (제목, 활성여부, 기간 등)
     * @param pageable  페이징 정보 (페이지 번호, 크기, 정렬 조건 등)
     * @return 조건에 맞는 광고 DTO 목록 (페이징 처리된 결과)
     */
    Page<AdResDto> searchAds(AdSearchDto searchDto, Pageable pageable);
}