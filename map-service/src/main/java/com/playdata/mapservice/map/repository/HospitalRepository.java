package com.playdata.mapservice.map.repository;

import com.playdata.mapservice.map.entity.AnimalHospital;
import com.playdata.mapservice.map.entity.HospitalAddressCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends JpaRepository<AnimalHospital, Long> {

    // 특정 지역명으로 시작하는 모든 병원들을 리턴
    // statusCode가 1이여야 영업중임.
    @Query("SELECT a FROM AnimalHospital a WHERE a.fullAddress LIKE concat(:region, '%') " +
            "AND a.businessStatusCode = '1'")
    List<AnimalHospital> findByRegion(@Param("region") String region);
}
