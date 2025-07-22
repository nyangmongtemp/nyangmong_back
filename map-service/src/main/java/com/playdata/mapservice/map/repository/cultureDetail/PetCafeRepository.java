package com.playdata.mapservice.map.repository.cultureDetail;

import com.playdata.mapservice.map.entity.CultureDetail.PetCafe;
import com.playdata.mapservice.map.entity.CultureDetail.PetStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PetCafeRepository extends JpaRepository<PetCafe, Long> {

    // 시도 값을 받아서 중복되지 않는 시군구 값을 리턴
    @Query("SELECT DISTINCT p.sigungu FROM PetCafe p WHERE p.sido = :sido")
    List<String> findDetailRegion(@Param("sido") String targetRegion);

    @Query("SELECT p FROM PetCafe p WHERE p.sido = :sido AND p.sigungu = :sigungu")
    List<PetCafe> findListByRegion(@Param("sido") String desc, @Param("sigungu") String sigungu);

}
