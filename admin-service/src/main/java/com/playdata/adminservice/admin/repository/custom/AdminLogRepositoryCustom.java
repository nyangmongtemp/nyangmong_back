package com.playdata.adminservice.admin.repository.custom;

import com.playdata.adminservice.admin.dto.req.AdminLogSearchDto;
import com.playdata.adminservice.admin.dto.res.AdminLogListResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminLogRepositoryCustom {

    /**
     * 관리자 로그 목록을 검색 조건과 페이징 정보로 조회
     * 
     * @param searchDto - 검색 조건 DTO
     * @param pageable - 페이징 정보
     * @return 관리자 로그 목록 페이징 결과
     */
    Page<AdminLogListResDto> findByAdminLogList(AdminLogSearchDto searchDto, Pageable pageable);
}
