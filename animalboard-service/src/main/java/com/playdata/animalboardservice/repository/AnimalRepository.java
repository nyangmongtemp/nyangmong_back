package com.playdata.animalboardservice.repository;

import com.playdata.animalboardservice.entity.Animal;
import com.playdata.animalboardservice.repository.custom.AnimalRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long>, AnimalRepositoryCustom {

}
