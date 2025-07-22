package com.playdata.adminservice.admin.repository.custom;

import com.playdata.adminservice.admin.dto.req.UserSearchDto;
import com.playdata.adminservice.admin.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    /**
     * [관리자] - 사용자 목록 조회 (검색, 페이징)
     * @param userSearchDto
     * @param pageable
     * @return
     */
    Page<User> findList(UserSearchDto userSearchDto, Pageable pageable);
}
