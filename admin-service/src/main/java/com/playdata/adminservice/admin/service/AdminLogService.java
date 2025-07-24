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

    public Page<AdminLogListResDto> findAdminLogList(AdminLogSearchDto searchDto, Pageable pageable) {
        return adminLogRepository.findByAdminLogList(searchDto, pageable);
    }
}
