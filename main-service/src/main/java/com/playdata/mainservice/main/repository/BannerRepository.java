package com.playdata.mainservice.main.repository;

import com.playdata.mainservice.main.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner,Long> {

    // 활성화가 되어있고, order 값이 있는 (노출되는) 모든 배너(기본 배너 포함)를 리턴
    // order는 작을수록 우선순위가 높은 것임
    @Query("SELECT b FROM Banner b WHERE b.active = true AND b.orderNum IS NOT NULL ORDER BY b.orderNum ASC")
    List<Banner> getExposedBanners();

}
