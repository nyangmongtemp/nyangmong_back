package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.AdminLogSearchDto;
import com.playdata.adminservice.admin.dto.res.AdminLogListResDto;
import com.playdata.adminservice.admin.service.AdminLogService;
import com.playdata.adminservice.common.dto.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/log")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'CUSTOMER')")
public class AdminLogController implements AdminLogControllerDocs{

    private final AdminLogService adminLogService;

    /**
     * 사용자 상세를 열람한 관리자들 로그 목록 조회 API
     *
     * @param searchDto - 검색 조건 DTO (검색어 등)
     * @param pageable - 페이징 및 정렬 정보
     * @return 검색된 관리자 로그 페이지 결과를 포함한 CommonResDto 반환
     */
    @GetMapping("/list")
    public ResponseEntity<?> getAdminLogList(AdminLogSearchDto searchDto, Pageable pageable) {
        Page<AdminLogListResDto> result = adminLogService.findAdminLogList(searchDto, pageable);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "목록 조회", result);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

}
