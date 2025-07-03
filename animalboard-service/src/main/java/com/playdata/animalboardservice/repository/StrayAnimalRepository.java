package com.playdata.animalboardservice.repository;

import com.playdata.animalboardservice.entity.StrayAnimal;
import com.playdata.animalboardservice.repository.custom.StrayAnimalRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StrayAnimalRepository extends JpaRepository<StrayAnimal, String>, StrayAnimalRepositoryCustom {

    /**
     * 유기동물 상세조회
     * @param desertionNo
     * @return
     */
    StrayAnimal findByDesertionNo(String desertionNo);

}
