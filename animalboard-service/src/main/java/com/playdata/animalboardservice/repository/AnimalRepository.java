package com.playdata.animalboardservice.repository;

import com.playdata.animalboardservice.dto.res.AnimalListResDto;
import com.playdata.animalboardservice.entity.Animal;
import com.playdata.animalboardservice.repository.custom.AnimalRepositoryCustom;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnimalRepository extends JpaRepository<Animal, Long>, AnimalRepositoryCustom {

    /**
     * 분양게시물 상세조회
     * @param postId 게시물 번호
     * @return
     */
    Animal findByPostIdAndActiveTrue(Long postId);

    /**
     * 특정 사용자가 작성한 모든 게시물(Animal)을 조회합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @return 해당 사용자가 작성한 게시물 리스트를 Optional로 감싼 형태 (없을 경우 Optional.empty())
     */
    Optional<List<Animal>> findByUserId(Long userId);

    // 마이페이지 용 페이징 조회 메소드 made by 이은혁
    @Query("""
    SELECT new com.playdata.animalboardservice.dto.res.AnimalListResDto(
        a.postId, a.userId, a.thumbnailImage, a.title, a.content, a.viewCount,
        a.petCategory, a.petKind, a.age, a.vaccine, a.sexCode, a.neuterYn,
        a.address, a.fee, a.active, a.reservationStatus
    )
    FROM Animal a
    WHERE a.userId = :userId AND a.active = true
""")
    Page<AnimalListResDto> findMyPost(@Param("userId") Long userId, Pageable pageable);
}
