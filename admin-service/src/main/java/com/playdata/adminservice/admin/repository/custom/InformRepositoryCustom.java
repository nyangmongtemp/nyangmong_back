package com.playdata.adminservice.admin.repository.custom;

import com.playdata.adminservice.admin.dto.req.SearchDto;
import com.playdata.adminservice.admin.dto.res.InformListResDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InformRepositoryCustom {

    Page<InformListResDto> findByInformList(SearchDto searchDto, Pageable pageable);
}
