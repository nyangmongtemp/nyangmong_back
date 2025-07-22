package com.playdata.adminservice.admin.repository.custom;

import com.playdata.adminservice.admin.dto.req.UserSearchDto;
import com.playdata.adminservice.admin.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    Page<User> findList(UserSearchDto userSearchDto, Pageable pageable);
}
