package com.playdata.adminservice.admin.repository;

import com.playdata.adminservice.admin.entity.Admin;
import com.playdata.adminservice.admin.repository.custom.AdminRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Long>, AdminRepositoryCustom {
    
    // email 정보를 통한 회원정보 조회
    Optional<Admin> findByEmail(String email);
}
