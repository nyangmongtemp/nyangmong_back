package com.playdata.adminservice.admin.repository.custom;

import com.playdata.adminservice.admin.dto.res.ReportListResDto;
import java.util.List;

public interface ReportRepositoryCustom {

    /**
     * 사용자의 신고내역 조회
     *
     * @param userId
     * @return
     */
    List<ReportListResDto> findReportList(Long userId);
}
