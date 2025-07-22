package com.playdata.adminservice.admin.repository;

import com.playdata.adminservice.admin.entity.Advertisement;
import com.playdata.adminservice.admin.repository.custom.AdvertisementRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 광고(Advertisement) 엔티티를 위한 JPA 레포지토리 인터페이스
 *
 * - JpaRepository를 상속받아 기본적인 CRUD 기능을 제공
 * - AdvertisementRepositoryCustom을 상속받아 QueryDSL 기반의 동적 검색 기능도 포함
 */
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>, AdvertisementRepositoryCustom {



    // JpaRepository: 기본 CRUD 메서드 제공 (findById, save, delete 등)
    // AdvertisementRepositoryCustom: 커스텀 검색 쿼리 (QueryDSL 기반)


}