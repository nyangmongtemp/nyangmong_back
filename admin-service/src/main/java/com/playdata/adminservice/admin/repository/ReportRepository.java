package com.playdata.adminservice.admin.repository;

import com.playdata.adminservice.admin.entity.Report;
import com.playdata.adminservice.admin.repository.custom.ReportRepositoryCustom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {

    /**
     * 사용자 신고내역 id와 신고처리여부 false 확인
     *
     * @param id
     * @return
     */
    Optional<Report> findByReportIdAndTreatIsFalse(long id);

    List<Report> findAllByAccusedUserIdAndTreatIsFalse(Long accusedUserId);
}
