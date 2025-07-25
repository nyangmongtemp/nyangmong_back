package com.playdata.adminservice.admin.repository;

import com.playdata.adminservice.admin.entity.InformationBoard;
import com.playdata.adminservice.admin.repository.custom.AdminBoardCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AdminBoardRepository
 * - InformationBoard 엔티티에 대한 기본 CRUD 기능 제공 (JpaRepository 상속)
 * - 커스텀 게시판 검색/조회 기능 제공 (AdminBoardCustomRepository 상속)
 *
 *   현재 이 Repository는 'InformationBoard' 엔티티에만 종속되어 있음
 * - IntroductionBoard, Animal 등 다른 게시판은 별도 Repository 필요
 * - 전체 게시판 통합 검색/조회는 CustomRepository 구현체에서 처리
 */
public interface AdminBoardRepository extends JpaRepository<InformationBoard, Long>, AdminBoardCustomRepository {
}