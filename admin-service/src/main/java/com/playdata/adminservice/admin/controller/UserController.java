package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.UserSearchDto;
import com.playdata.adminservice.admin.dto.res.UserDetailResDto;
import com.playdata.adminservice.admin.dto.res.UserListResDto;
import com.playdata.adminservice.admin.service.UserService;
import com.playdata.adminservice.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'CONTENT')")
public class UserController {

    private final UserService userService;

    /**
     * [관리자] - 사용자 목록 조회 (검색, 페이징)
     * @param userSearchDto 검색조건
     * @param pageable 페이지
     * @return
     */
    @GetMapping("/user/list")
    public ResponseEntity<CommonResDto> userList(UserSearchDto userSearchDto, Pageable pageable) {
        Page<UserListResDto> pageResult = userService.findUserList(userSearchDto, pageable);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "목록 조회", pageResult);
        return ResponseEntity.ok(resDto);
    }

    /**
     * [관리자] - 사용자 상세 조회
     * @param id
     * @return
     */
    @GetMapping("/user/detail/{id}")
    public ResponseEntity<CommonResDto> userDetail(@PathVariable long id) {
        UserDetailResDto result = userService.findUser(id);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "상세 조회", result);
        return ResponseEntity.ok(resDto);
    }

}
