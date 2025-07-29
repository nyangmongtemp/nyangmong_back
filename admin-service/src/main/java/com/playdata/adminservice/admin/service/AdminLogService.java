package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.AdminLogSearchDto;
import com.playdata.adminservice.admin.dto.res.AdminLogListResDto;
import com.playdata.adminservice.admin.repository.AdminLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminLogService {

    private final AdminLogRepository adminLogRepository;

    /**
     * 관리자 로그 목록 조회 서비스 메서드
     *
     * @param searchDto - 검색 조건 DTO
     * @param pageable - 페이징 정보
     * @return 조건에 맞는 관리자 로그 목록 페이징 결과 반환
     */
    public Page<AdminLogListResDto> findAdminLogList(AdminLogSearchDto searchDto, Pageable pageable) {
        return adminLogRepository.findByAdminLogList(searchDto, pageable);
    }
}
