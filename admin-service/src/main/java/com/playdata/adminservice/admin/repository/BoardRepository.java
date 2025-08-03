package com.playdata.adminservice.admin.repository;


import com.playdata.adminservice.admin.entity.Board;
import com.playdata.adminservice.admin.entity.Category;
import com.playdata.adminservice.admin.repository.custom.BoardRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long>, BoardRepositoryCustom {

    // 게시물 상세 조회 시 postId, category를 조회
    Board findByPostIdAndCategoryAndActiveTrue(Long postId, Category category);

    // 사용자의 상태 (비활성화, 사용자 닉네임 변경 등) 가 변경 되었을때 그 사용자 정보 조회
    @Query("SELECT i FROM Board i WHERE i.userId = :userId")
    List<Board> findByUserId(@Param("userId") Long userId);

    @Query("SELECT i FROM Board i WHERE i.userId = :userId AND i.category = :category " +
            "AND i.active = true")
    Page<Board> findMyPost(@Param("userId") Long userId,
                                      @Param("category") Category targetCategory, Pageable pageable);


}
