package com.playdata.adminservice.admin.repository.custom;

import com.playdata.adminservice.admin.dto.AdminSearchDto;
import com.playdata.adminservice.admin.entity.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminRepositoryCustom {

    /**
     *
     * @param adminSearchDto
     * @param pageable
     * @return
     */
    // 관리자 목록 조회
    Page<Admin> findList(AdminSearchDto adminSearchDto, Pageable pageable);

}
