package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.TermsInsertReqDto;
import com.playdata.adminservice.admin.entity.Terms;
import com.playdata.adminservice.admin.repository.TermsRepository;
import com.playdata.adminservice.common.auth.TokenUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;

    /**
     *
     * @param userInfo
     * @param termsInsertReqDto
     * @return
     */
    public Terms insertTerms(TokenUserInfo userInfo, @Valid TermsInsertReqDto termsInsertReqDto) {
        Long adminId = userInfo.getAdminId();
        return termsRepository.save(termsInsertReqDto.toEntity(adminId));
    }
}
