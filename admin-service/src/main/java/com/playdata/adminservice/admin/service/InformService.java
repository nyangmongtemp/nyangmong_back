package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.InformReplyReqDto;
import com.playdata.adminservice.admin.dto.req.InformSearchDto;
import com.playdata.adminservice.admin.dto.res.InformDetailResDto;
import com.playdata.adminservice.admin.dto.res.InformListResDto;
import com.playdata.adminservice.admin.entity.Inform;
import com.playdata.adminservice.admin.repository.InformRepository;
import com.playdata.adminservice.common.auth.TokenUserInfo;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import com.playdata.adminservice.common.exception.CommonException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<InformListResDto> findInformList(InformSearchDto searchDto, Pageable pageable) {
        return informRepository.findByInformList(searchDto, pageable);
    }

    /**
     * 문의 상세 정보 조회
     *
     * @param id 문의 ID
     * @return 상세 DTO
     */
    public InformDetailResDto informDetail(Long id) {
        return informRepository.findByInform(id);
    }

    /**
     * 문의 답변
     *
     * @param informReplyReqDto
     * @return
     */
    @Transactional
    public Inform replyInform(Long id, InformReplyReqDto informReplyReqDto, TokenUserInfo adminInfo) {
        Inform inform = informRepository.findById(id).orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));
        inform.replyInform(informReplyReqDto, adminInfo.getAdminId());
        return inform;
    }
}
