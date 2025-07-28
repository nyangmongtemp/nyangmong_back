package com.playdata.adminservice.admin.repository;

import com.playdata.adminservice.admin.entity.Report;
import com.playdata.adminservice.admin.repository.custom.ReportRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {
}
