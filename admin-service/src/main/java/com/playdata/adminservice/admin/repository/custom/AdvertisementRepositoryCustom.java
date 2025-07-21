package com.playdata.adminservice.admin.repository.custom;

import com.playdata.adminservice.admin.dto.req.AdSearchDto;
import com.playdata.adminservice.admin.dto.res.AdResDto;
import com.playdata.adminservice.admin.entity.Advertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 광고 검색 및 커스텀 쿼리용 인터페이스
 * QueryDSL 기반 동적 쿼리를 정의합니다.
 */
public interface AdvertisementRepositoryCustom {

    /**
     * 특정 광고 ID들을 제외하고, 주어진 orderNum 리스트와 중복되는 광고를 조회합니다.
     *
     * @param orderNums 중복 여부를 확인할 orderNum 리스트
     * @param excludeIds 제외할 광고 ID 리스트
     * @return 중복되는 광고 리스트
     */
    List<Advertisement> findConflictingOrderNums(List<Integer> orderNums, List<Long> excludeIds);

    /**
     * 광고 목록을 조건과 페이징 정보로 조회합니다.
     *
     * @param searchDto 검색 조건 DTO (제목, 활성 여부, 기간 등)
     * @param pageable 페이징 정보
     * @return 조건에 맞는 광고 목록의 페이징 결과
     */
    Page<Advertisement> searchAds(AdSearchDto searchDto, Pageable pageable);

    /**
     * 특정 orderNum 보다 큰 orderNum을 가진 광고들을 조회합니다.
     *
     * @param orderNum 기준 orderNum
     * @return 조건에 맞는 광고 리스트
     */
    List<Advertisement> findByOrderNumGreaterThan(Integer orderNum);

    /**
     * 특정 orderNum이 활성 광고에 존재하는지 여부를 확인합니다.
     *
     * @param orderNum 확인할 orderNum
     * @return 존재하면 true, 아니면 false
     */
    boolean existsByOrderNum(Integer orderNum);

}