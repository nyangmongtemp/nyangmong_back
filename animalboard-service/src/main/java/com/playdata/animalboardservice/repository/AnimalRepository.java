package com.playdata.animalboardservice.repository;

import com.playdata.animalboardservice.entity.Animal;
import com.playdata.animalboardservice.repository.custom.AnimalRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long>, AnimalRepositoryCustom {

    /**
     * 분양게시물 상세조회
     * @param postId 게시물 번호
     * @return
     */
    Animal findByPostId(Long postId);

}
