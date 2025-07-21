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
        // 1. 중복된 순서가 있는지 확인
        boolean exists = adRepository.existsByOrderNum(dto.getOrderNum());
        if (exists) {
            throw new IllegalStateException("이미 존재하는 순서 번호입니다: " + dto.getOrderNum());
        }

        // 2. 광고 엔티티 생성 및 저장
        Advertisement ad = Advertisement.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .active(dto.getActive())
                .orderNum(dto.getOrderNum())
                .thumbnailImage(dto.getThumbnailImage())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .linkUrl(dto.getLinkUrl())
                .build();

        Advertisement saved = adRepository.save(ad);

        // 3. 결과 반환
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

        ad.update(dto.getTitle(), dto.getDescription(), dto.getActive(), dto.getOrderNum(),
                dto.getThumbnailImage(), dto.getStartDate(), dto.getEndDate(), dto.getLinkUrl());

        return new CommonResDto(HttpStatus.OK, "광고 수정 완료", ad);
    }

    /**
     * 광고 삭제 (active = false 방식)
     *
     * @param id 삭제할 광고 ID
     * @return 공통 응답 DTO
     */
    @Transactional
    public CommonResDto deleteAd(Long id) {
        // 1. ID로 광고 조회 (없으면 예외 발생)
        Advertisement ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 광고가 존재하지 않습니다."));

        Integer deletedOrderNum = ad.getOrderNum();

        // 2. 광고 비활성화 처리 및 순서 번호 제거
        ad.setActive(false);           // 실제 삭제하지 않고 active = false 처리
        ad.setOrderNum(null);         // 삭제된 광고의 순서 번호 제거

        // 3. 삭제된 광고보다 뒤에 있는 광고들의 순서를 한 칸씩 앞으로 당김
        List<Advertisement> adsToUpdate = adRepository.findByOrderNumGreaterThan(deletedOrderNum);

        for (Advertisement a : adsToUpdate) {
            a.setOrderNum(a.getOrderNum() - 1); // 순서 번호 -1
        }

        // 4. 결과 반환
        return new CommonResDto(HttpStatus.OK, "광고 삭제 처리 및 순서 재정렬 완료", null);
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

    @Transactional
    public void updateAdOrder(List<AdOrderReqDto> orderDtoList) {
        // 1. 요청 내 중복 순서 체크
        Set<Integer> orderSet = new HashSet<>();
        for (AdOrderReqDto dto : orderDtoList) {
            if (!orderSet.add(dto.getOrderNum())) {
                throw new IllegalArgumentException("요청에 중복된 순서 번호가 있습니다: " + dto.getOrderNum());
            }
        }

        // 2. DB에 이미 존재하는 동일한 orderNum 체크 (요청 대상 외 광고)
        List<Integer> orderNums = orderDtoList.stream()
                .map(AdOrderReqDto::getOrderNum)
                .toList();

        List<Long> updateIds = orderDtoList.stream()
                .map(AdOrderReqDto::getId)
                .toList();

        // 현재 수정하려는 광고(id) 외에, 동일한 orderNum이 존재하는지 검사
        List<Advertisement> duplicatesInDb = adRepository
                .findConflictingOrderNums(orderNums, updateIds);

        if (!duplicatesInDb.isEmpty()) {
            String conflictInfo = duplicatesInDb.stream()
                    .map(ad -> "[id=" + ad.getId() + ", orderNum=" + ad.getOrderNum() + "]")
                    .collect(Collectors.joining(", "));
            throw new IllegalStateException("DB에 중복된 순서 번호가 이미 존재합니다: " + conflictInfo);
        }

        // 3. 순서 업데이트
        for (AdOrderReqDto dto : orderDtoList) {
            Advertisement ad = adRepository.findById(dto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("ID " + dto.getId() + "인 광고가 존재하지 않습니다."));
            ad.setOrderNum(dto.getOrderNum());
        }
    }


}