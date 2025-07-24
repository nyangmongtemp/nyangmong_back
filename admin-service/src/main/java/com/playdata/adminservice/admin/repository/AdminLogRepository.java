package com.playdata.adminservice.admin.repository;

import com.playdata.adminservice.admin.entity.AdminLog;
import com.playdata.adminservice.admin.repository.custom.AdminLogRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminLogRepository extends JpaRepository<AdminLog, Long>, AdminLogRepositoryCustom {

}
