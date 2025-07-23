package com.playdata.userservice.user.repository;

import com.playdata.userservice.user.entity.Inform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InformRepository extends JpaRepository<Inform, Long> {

    // 본인의 활성화된 모든 문의 리턴 (회원 탈퇴 진행 시)
    @Query("SELECT i FROM Inform i WHERE i.userId = :userId AND i.active = true")
    Optional<List<Inform>> getMyActiveInform(@Param("userId") Long userId);

    // 본인의 활성화된 문의 페이징 조회
    @Query("SELECT i FROM Inform i WHERE i.userId = :userId AND i.active = true AND i.answered = :answered")
    Page<Inform> findMyInform(@Param("userId") Long userId, Pageable pageable,@Param("answered") boolean flag);
}
