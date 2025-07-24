package com.playdata.adminservice.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.playdata.adminservice.admin.entity.Banner;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

    // 활성화 되어있고, order값이 있는 (노출되는) 배너의 개수를 리턴
    // int가 아닌 Integer로 리턴하는 이유는 리턴값이 null일 수도 있기 때문
    @Query("SELECT COUNT(b) FROM Banner b WHERE b.active = true AND b.orderNum IS NOT NULL ")
    Integer countExposedBanners();

    // 활성화 되어있고, order값이 있는 배너 중 order의 최대값을 리턴
    // int가 아닌 Integer로 리턴하는 이유는 리턴값이 null일 수도 있기 때문
    @Query("SELECT MAX(b.orderNum) FROM Banner b WHERE b.active = true AND b.orderNum IS NOT NULL ")
    Integer getMaxOrder();

    // 활성화가 되어있고, order 값이 있는 (노출되는) 모든 배너(기본 배너 포함)를 리턴
    // order는 작을수록 우선순위가 높은 것임
    @Query("SELECT b FROM Banner b WHERE b.active = true AND b.orderNum IS NOT NULL ORDER BY b.orderNum ASC")
    List<Banner> getExposedBanners();

    // 기본 배너 조회
    // ASC인 이유는 반대방향으로 list에 add되기 때문
    @Query("SELECT b FROM Banner b WHERE b.active = true AND b.basic = true ORDER BY b.createAt ASC ")
    List<Banner> getBasicBanner();
    
    // 노출 등록을 위한 조회메소드
    // 제목을 통한 검색  --> keyword가 없으면, 전체 조회
    @Query("""
    SELECT b FROM Banner b 
    WHERE b.active = true 
    AND (:keyword IS NULL OR :keyword = '' OR b.title LIKE CONCAT('%', :keyword, '%'))
    """)
    Page<Banner> getExposedBannersByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
