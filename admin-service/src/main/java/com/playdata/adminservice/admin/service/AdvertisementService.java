package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.AdRegisterReqDto;
import com.playdata.adminservice.admin.dto.req.AdSearchDto;
import com.playdata.adminservice.admin.dto.res.AdResDto;
import com.playdata.adminservice.admin.entity.Advertisement;
import com.playdata.adminservice.admin.repository.AdvertisementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 광고 서비스 클래스
 * 광고 등록, 수정, 삭제, 단건 조회, 목록 조회 기능을 제공
 */
@Service
@RequiredArgsConstructor
public class AdvertisementService {

    // 광고 Repository 주입
    private final AdvertisementRepository adRepository;

    /**
     * 광고 등록
     * @param dto 광고 등록 요청 DTO
     * @return 등록된 광고 정보를 담은 응답 DTO
     */
    public AdResDto registerAd(AdRegisterReqDto dto) {
        Advertisement ad = new Advertisement();
        ad.update(dto.getTitle(), dto.getDescription(), dto.getActive(), dto.getOrderNum(),
                dto.getThumbnailImage(), dto.getStartDate(), dto.getEndDate());
        Advertisement saved = adRepository.save(ad); // DB에 저장
        return convertToDto(saved); // 응답 DTO로 변환 후 반환
    }

    /**
     * 광고 수정
     * @param id 수정할 광고 ID
     * @param dto 광고 수정 요청 DTO
     * @return 수정된 광고 응답 DTO
     */
    public AdResDto updateAd(Long id, AdRegisterReqDto dto) {
        Advertisement ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("광고가 존재하지 않습니다."));
        ad.update(dto.getTitle(), dto.getDescription(), dto.getActive(), dto.getOrderNum(),
                dto.getThumbnailImage(), dto.getStartDate(), dto.getEndDate());
        return convertToDto(ad);
    }

    /**
     * 광고 삭제
     * @param id 삭제할 광고 ID
     */
    public void deleteAd(Long id) {
        adRepository.deleteById(id);
    }

    /**
     * 광고 단건 조회
     * @param id 조회할 광고 ID
     * @return 조회된 광고 응답 DTO
     */
    public AdResDto getAd(Long id) {
        Advertisement ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("광고가 존재하지 않습니다."));
        return convertToDto(ad);
    }

    /**
     * 광고 검색 + 페이징 조회
     * @param searchDto 검색 조건 DTO
     * @param pageable 페이징 정보
     * @return 페이징된 광고 목록
     */
    public Page<AdResDto> searchAds(AdSearchDto searchDto, Pageable pageable) {
        return adRepository.searchAds(searchDto, pageable);
    }

    /**
     * Advertisement 엔티티를 AdResDto로 변환
     */
    private AdResDto convertToDto(Advertisement ad) {
        return new AdResDto(ad.getId(), ad.getThumbnailImage(), ad.getTitle(), ad.getDescription(),
                ad.getActive(), ad.getOrderNum(), ad.getStartDate(), ad.getEndDate(),
                ad.getCreatedAt(), ad.getUpdatedAt());
    }
}