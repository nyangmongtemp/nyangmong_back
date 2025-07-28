package com.playdata.adminservice.admin.repository;

import com.playdata.adminservice.admin.entity.IntroductionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntroductionBoardRepository extends JpaRepository<IntroductionBoard, Long> {
}