package com.playdata.mapservice.map.repository;

import com.playdata.mapservice.map.entity.AddressCode;
import com.playdata.mapservice.map.entity.ContentType;
import com.playdata.mapservice.map.entity.MapEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MapEntity에 대한 DB 접근 인터페이스(JPA Repository)
 * 기본 CRUD 기능 제공 + title 기반 조회/삭제 기능 추가
 */
@Repository
public interface MapRepository extends JpaRepository<MapEntity, String> {

    /**
     * contentId 기준으로 지도 데이터 조회
     * @param contentId 컨텐츠id
     * @return Optional<MapEntity> - 존재하면 동물 엔티티 반환
     */
    Optional<MapEntity> findByContentId(String contentId);

    // 지역과 컨텐트 타입을 통한 모든 Map 객체를 리턴하는 메소드
    @Query("SELECT m FROM MapEntity m WHERE m.addressCode = :addr AND m.contentType = :content")
    List<MapEntity> findByTypeAndRegionList(@Param("content") ContentType contentType,
                                            @Param("addr") AddressCode addressCode);

    @Query("SELECT m FROM MapEntity m WHERE m.mapId = :mapId")
    Optional<MapEntity> findByMapId(@Param("mapId") Long mapId);
}