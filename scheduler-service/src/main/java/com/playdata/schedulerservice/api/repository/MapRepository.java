package com.playdata.schedulerservice.api.repository;

import com.playdata.schedulerservice.api.entity.ContentType;
import com.playdata.schedulerservice.api.entity.MapEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MapEntity에 대한 DB 접근 인터페이스(JPA Repository)
 * 기본 CRUD 기능 제공 + title 기반 조회/삭제 기능 추가
 */
public interface MapRepository extends JpaRepository<MapEntity, String> {

    /**
     * contentId 기준으로 지도 데이터 조회
     * @param contentId 컨텐츠id
     * @return Optional<MapEntity> - 존재하면 동물 엔티티 반환
     */
    Optional<MapEntity> findByContentId(String contentId);
}