package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.req.SearchDto;
import com.playdata.adminservice.admin.dto.res.InformDetailResDto;
import com.playdata.adminservice.admin.dto.res.InformListResDto;
import com.playdata.adminservice.admin.dto.res.TermsListResDto;
import com.playdata.adminservice.admin.service.InformService;
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
@RequestMapping("/admin/inform")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', 'CUSTOMER')")
public class InformController {

    private final InformService informService;

    /**
     * 문의 목록 조회 (검색 + 페이징 지원)
     *
     * @param searchDto 검색 조건 DTO
     * @param pageable 페이징 정보
     * @return 문의 목록 페이지
     */
    @GetMapping("/list")
    public ResponseEntity<CommonResDto> getInformList(SearchDto searchDto, Pageable pageable) {
        Page<InformListResDto> result = informService.findInformList(searchDto, pageable);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "목록 조회", result);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * 문의 상세 조회
     *
     * @param id 문의 ID
     * @return 상세 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommonResDto> getInform(@PathVariable Long id) {
        InformDetailResDto result = informService.informDetail(id);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "상세 조회", result);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }


}
