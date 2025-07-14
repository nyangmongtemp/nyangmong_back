package com.playdata.festivalservice.repository;

import com.playdata.festivalservice.entity.FestivalEntity;
import com.playdata.festivalservice.repository.custom.FestivalRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FestivalRepository extends JpaRepository<FestivalEntity, Long> , FestivalRepositoryCustom {

}