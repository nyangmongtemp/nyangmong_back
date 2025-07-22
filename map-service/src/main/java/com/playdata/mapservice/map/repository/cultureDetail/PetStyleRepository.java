package com.playdata.mapservice.map.repository.cultureDetail;

import com.playdata.mapservice.map.entity.CultureDetail.CultureAddressCode;
import com.playdata.mapservice.map.entity.CultureDetail.PetStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetStyleRepository extends JpaRepository<PetStyle, Long> {
    
    // 시도 값을 받아서 중복되지 않는 시군구 값을 리턴
    @Query("SELECT DISTINCT p.sigungu FROM PetStyle p WHERE p.sido = :sido")
    List<String> findDetailRegion(@Param("sido") String targetRegion);

    @Query("SELECT p FROM PetStyle p WHERE p.sido = :sido AND p.sigungu = :sigungu")
    List<PetStyle> findListByRegion(@Param("sido") String desc, @Param("sigungu") String sigungu);
}
