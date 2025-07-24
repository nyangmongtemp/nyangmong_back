package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.SearchDto;
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

    public Page<InformListResDto> findInformList(SearchDto searchDto, Pageable pageable) {
        return informRepository.findByInformList(searchDto, pageable);
    }
}
