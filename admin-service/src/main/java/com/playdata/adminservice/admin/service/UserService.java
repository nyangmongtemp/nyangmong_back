package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.UserSearchDto;
import com.playdata.adminservice.admin.dto.res.UserDetailResDto;
import com.playdata.adminservice.admin.dto.res.UserListResDto;
import com.playdata.adminservice.admin.entity.User;
import com.playdata.adminservice.admin.repository.UserRepository;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import com.playdata.adminservice.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * [관리자] - 사용자 목록 조회 (검색, 페이징)
     * @param userSearchDto
     * @param pageable
     * @return
     */
    public Page<UserListResDto> findUserList(UserSearchDto userSearchDto, Pageable pageable) {
        Page<User> userList =  userRepository.findList(userSearchDto, pageable);

        return userList.map(user ->
                UserListResDto.builder()
                        .user(user)
                        .build()
        );
    }

    public UserDetailResDto findUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));
        return new UserDetailResDto(user);
    }
}
