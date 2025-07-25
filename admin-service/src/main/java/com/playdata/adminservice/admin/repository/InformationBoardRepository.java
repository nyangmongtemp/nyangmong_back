package com.playdata.adminservice.admin.repository;

import com.playdata.adminservice.admin.entity.InformationBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformationBoardRepository extends JpaRepository<InformationBoard, Long> {
}