package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.SearchDto;
import com.playdata.adminservice.admin.dto.res.InformDetailResDto;
import com.playdata.adminservice.admin.dto.res.InformListResDto;
import com.playdata.adminservice.admin.repository.InformRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InformService {

    private final InformRepository informRepository;

    /**
     * 검색 및 페이징된 문의 리스트 조회
     *
     * @param searchDto 검색어 등 조건
     * @param pageable 페이징 정보
     * @return 페이지 결과
     */
    public Page<InformListResDto> findInformList(SearchDto searchDto, Pageable pageable) {
        return informRepository.findByInformList(searchDto, pageable);
    }

    /**
     * 특정 문의 상세 정보 조회
     *
     * @param id 문의 ID
     * @return 상세 DTO
     */
    public InformDetailResDto informDetail(Long id) {
        return informRepository.findByInform(id);
    }
}
