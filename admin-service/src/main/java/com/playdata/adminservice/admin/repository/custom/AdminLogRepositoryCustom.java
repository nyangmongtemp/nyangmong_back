package com.playdata.adminservice.admin.repository.custom;

import com.playdata.adminservice.admin.dto.req.AdminLogSearchDto;
import com.playdata.adminservice.admin.dto.res.AdminLogListResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminLogRepositoryCustom {

    Page<AdminLogListResDto> findByAdminLogList(AdminLogSearchDto searchDto, Pageable pageable);
}
