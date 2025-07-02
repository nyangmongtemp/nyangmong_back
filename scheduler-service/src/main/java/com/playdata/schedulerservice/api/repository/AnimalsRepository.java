package com.playdata.schedulerservice.api.repository;

import com.playdata.schedulerservice.api.entity.StrayAnimalEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AnimalsEntity에 대한 DB 접근 인터페이스(JPA Repository)
 * 기본 CRUD 기능 제공 + desertionNo 기반 조회/삭제 기능 추가
 */
public interface AnimalsRepository extends JpaRepository<StrayAnimalEntity, String> {

    /**
     * desertionNo를 기준으로 동물 데이터 조회
     * @param desertionNo 유기번호 (유니크)
     * @return Optional<AnimalsEntity> - 존재하면 동물 엔티티 반환
     */
    Optional<StrayAnimalEntity> findByDesertionNo(String desertionNo);
}