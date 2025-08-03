package com.playdata.adminservice.admin.repository;


import com.playdata.adminservice.admin.dto.board.res.AnimalListResDto;
import com.playdata.adminservice.admin.entity.Animal;
import com.playdata.adminservice.admin.repository.custom.AnimalRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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
    @Query("SELECT a FROM Animal a WHERE a.userId = :userId AND a.active = true")
    Page<AnimalListResDto> findMyPost(@Param("userId") Long userId, Pageable pageable);
}
