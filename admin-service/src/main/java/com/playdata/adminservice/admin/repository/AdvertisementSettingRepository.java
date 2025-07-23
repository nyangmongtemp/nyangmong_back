package com.playdata.adminservice.admin.repository;

import com.playdata.adminservice.admin.entity.AdvertisementCount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 광고 설정(광고 개수 등)을 위한 Repository
 * AdvertisementCount 엔티티를 관리하는 JPA 리포지토리
 */
public interface AdvertisementSettingRepository extends JpaRepository<AdvertisementCount, Long> {

    /**
     * 광고 개수 설정 중 가장 최근의 데이터를 조회
     * - ID(adNumId)가 가장 큰 레코드를 반환
     * - 일반적으로 최신 설정값을 조회하는 데 사용
     *
     * @return 가장 마지막(adNumId 기준 내림차순) 설정 데이터
     */
    Optional<AdvertisementCount> findTopByOrderByAdNumIdDesc();
}