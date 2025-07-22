package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.AdOrderReqDto;
import com.playdata.adminservice.admin.dto.req.AdRegisterReqDto;
import com.playdata.adminservice.admin.dto.req.AdSearchDto;
import com.playdata.adminservice.admin.dto.req.AdUpdateReqDto;
import com.playdata.adminservice.admin.dto.res.AdResDto;
import com.playdata.adminservice.admin.entity.Advertisement;
import com.playdata.adminservice.admin.repository.AdvertisementRepository;
import com.playdata.adminservice.common.dto.CommonResDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 광고 서비스 클래스
 * 광고 등록, 수정, 삭제, 단건 조회, 목록 조회 기능을 제공
 */
@Getter
@Service
@RequiredArgsConstructor
public class AdvertisementService {

    // 광고 Repository 주입
    private final AdvertisementRepository adRepository;

    /**
     * 광고 등록
     *
     * @param dto 광고 등록 요청 DTO
     * @return 등록된 광고 정보를 담은 응답 DTO
     */
    @Transactional
    public CommonResDto registerAd(AdRegisterReqDto dto) {


        // 광고 엔티티 생성 및 저장
        Advertisement ad = Advertisement.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .active(dto.getActive())
                .confirmed(dto.getConfirmed())
                .thumbnailImage(dto.getThumbnailImage())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .linkUrl(dto.getLinkUrl())
                .build();


        Advertisement saved = adRepository.save(ad);

        //  결과 반환
        return new CommonResDto(HttpStatus.CREATED, "광고 등록 성공", saved);
    }

    /**
     * 광고 수정
     *
     * @param id  수정할 광고 ID
     * @param dto 광고 수정 요청 DTO
     * @return 수정된 광고 응답 DTO
     */
    @Transactional
    public CommonResDto updateAd(Long id, @Valid AdUpdateReqDto dto) {
        Advertisement ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("광고가 존재하지 않습니다."));

        ad.update(dto.getTitle(), dto.getDescription(), dto.getActive(), dto.getConfirmed(),
                dto.getThumbnailImage(), dto.getStartDate(), dto.getEndDate(), dto.getLinkUrl());

        return new CommonResDto(HttpStatus.OK, "광고 수정 완료", ad);
    }



    /**
     * 광고 단건 조회
     *
     * @param id 조회할 광고 ID
     * @return 조회된 광고 응답 DTO
     */
    public CommonResDto getAd(Long id) {
        Advertisement ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("광고가 존재하지 않습니다."));
        return new CommonResDto(HttpStatus.OK, "광고 상세 조회 성공", ad);
    }

    /**
     * 광고 검색 + 페이징 조회
     *
     * @param searchDto 검색 조건 DTO
     * @param pageable  페이징 정보
     * @return 페이징된 광고 목록
     */
    public Page<AdResDto> searchAds(AdSearchDto searchDto, Pageable pageable) {
        return adRepository.searchAds(searchDto, pageable)
                .map(AdResDto::from);
    }


}