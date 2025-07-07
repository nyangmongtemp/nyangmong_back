package com.playdata.festivalservice.repository;

import com.playdata.festivalservice.entity.FestivalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FestivalRepository extends JpaRepository<FestivalEntity, Long> {

    /**
     * 해시 값으로 중복된 축제가 있는지 확인할 때 사용
     */
    Optional<FestivalEntity> findByHash(String hash);

    /**
     * 축제 제목으로 검색
     */
    Optional<FestivalEntity> findByTitle(String title);

    // 필요한 경우 더 복잡한 쿼리도 아래처럼 작성 가능
    // List<FestivalEntity> findByFestivalDateBetween(String startDate, String endDate);
}