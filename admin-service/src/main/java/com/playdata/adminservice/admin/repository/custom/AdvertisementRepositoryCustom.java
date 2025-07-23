package com.playdata.adminservice.admin.repository.custom;

import com.playdata.adminservice.admin.dto.req.AdSearchDto;
import com.playdata.adminservice.admin.entity.Advertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 광고 관련 커스텀 레포지토리 인터페이스
 * QueryDSL 기반으로 복잡한 조건 검색 및 맞춤 쿼리를 정의
 */
public interface AdvertisementRepositoryCustom {

    /**
     * 광고 목록을 조건과 페이징 정보로 조회합니다.
     * - 제목, 활성 여부, 시작일/종료일 등 다양한 조건으로 필터링
     * - 페이징 처리 포함
     *
     * @param searchDto 광고 검색 조건 DTO
     * @param pageable Spring Data의 페이징 객체
     * @return 조건에 맞는 광고 엔티티들의 페이징 결과
     */
    Page<Advertisement> getAdsList(AdSearchDto searchDto, Pageable pageable);

    /**
     * 승인되었고(active = true) 활성화된 광고 전체 조회
     * - 주로 광고 노출용 리스트에서 사용
     *
     * @return 승인 & 활성화된 광고 리스트
     */
    List<Advertisement> findByConfirmedTrueAndActiveTrue();

    /**
     * 아직 승인되지 않은 광고 중에서 무작위로 일부 조회
     * - 승인 대기 중인 광고를 랜덤하게 샘플링할 때 사용
     *
     * @param limit 가져올 광고 개수 제한
     * @return 무작위로 선택된 미승인 광고 리스트
     */
    List<Advertisement> findByConfirmedFalseRandomLimit(int limit);
}