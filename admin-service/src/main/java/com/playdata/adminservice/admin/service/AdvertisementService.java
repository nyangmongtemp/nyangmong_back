package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.*;
import com.playdata.adminservice.admin.dto.res.AdResDto;
import com.playdata.adminservice.admin.entity.Advertisement;
import com.playdata.adminservice.admin.repository.AdvertisementRepository;
import com.playdata.adminservice.admin.repository.AdvertisementSettingRepository;
import com.playdata.adminservice.common.dto.CommonResDto;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import com.playdata.adminservice.common.exception.CommonException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.playdata.adminservice.admin.entity.AdvertisementCount;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 광고 서비스 클래스
 * 광고 등록, 수정, 삭제, 단건 조회, 목록 조회 기능을 제공
 */
@Getter
@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementService {

    // 광고 Repository 주입
    private final AdvertisementRepository adRepository;

    private final AdvertisementSettingRepository adSettingRepository;

    // application.yml에서 설정한 이미지 저장 경로를 주입받음
    @Value("C:/playdata2025/images")
    private String imageSaveUrl;

    /**
     * 광고 등록
     *
     * @param dto 광고 등록 요청 DTO
     * @return 등록된 광고 정보를 담은 응답 DTO
     *
     */
    @Transactional
    public CommonResDto registerAd(AdRegisterReqDto dto) {
        MultipartFile imageFile = dto.getThumbnailImage();
        String savedFileName = saveImage(imageFile);

        Advertisement ad = Advertisement.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .active(dto.getActive())
                .confirmed(dto.getConfirmed())
                .thumbnailImage(savedFileName) // 파일명 또는 접근 가능한 경로
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .linkUrl(dto.getLinkUrl())
                .build();

        Advertisement saved = adRepository.save(ad);
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
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));

        String updatedThumbnail = ad.getThumbnailImage(); // 기존 이미지 유지

        MultipartFile newFile = dto.getThumbnailImage();
        if (newFile != null && !newFile.isEmpty()) {
            updatedThumbnail = saveImage(newFile); // 새 이미지가 있으면 교체
        }

        ad.update(
                dto.getTitle(),
                dto.getDescription(),
                dto.getActive(),
                dto.getConfirmed(),
                updatedThumbnail,
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getLinkUrl()
        );

        return new CommonResDto(HttpStatus.OK, "광고 수정 완료", ad);
    }


    private String saveImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new CommonException(ErrorCode.EMPTY_FILE);
        }

        String originalFilename = imageFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = System.currentTimeMillis() + "_" + UUID.randomUUID() + extension;

        File saveDir = new File(imageSaveUrl);
        if (!saveDir.exists()) {
            boolean created = saveDir.mkdirs();
            if (!created) {
                throw new CommonException(ErrorCode.FILE_UPLOAD_FAIL, "이미지 저장 경로 생성 실패: " + imageSaveUrl);
            }
        }

        File savePath = new File(saveDir, uniqueFilename);

        try {
            imageFile.transferTo(savePath);
        } catch (IOException e) {
            throw new CommonException(ErrorCode.FILE_UPLOAD_FAIL, "이미지 저장 실패: " + e.getMessage());
        }

        return uniqueFilename;
    }



    /**
     * 광고 단건 조회
     *
     * @param id 조회할 광고 ID
     * @return 조회된 광고 응답 DTO
     */
    public CommonResDto getAd(Long id) {
        Advertisement ad = adRepository.findById(id)
         .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND)); //광고가 존재하지 않을경우 에러 메세지 출력
        return new CommonResDto(HttpStatus.OK, "광고 상세 조회 성공", ad);
    }

    /**
     * 광고 검색 + 페이징 조회
     *
     * @param searchDto 검색 조건 DTO
     * @param pageable  페이징 정보
     * @return 페이징된 광고 목록
     */
    public Page<AdResDto> getAdsList(AdSearchDto searchDto, Pageable pageable) {
        return adRepository.getAdsList(searchDto, pageable)
                .map(AdResDto::from);
    }





    // 광고 노출 개수 설정 변경
    @Transactional
    public CommonResDto updateAdCount(AdCountReqDto dto) {
        // 최근 설정된 광고 노출 개수를 조회 (없으면 새로 생성)
        AdvertisementCount setting = adSettingRepository.findTopByOrderByAdNumIdDesc()
                .orElse(AdvertisementCount.builder().build());
        // 새로운 광고 노출 개수로 설정값 업데이트
        setting.setAdNum(dto.getAdNum()); // 필드명 일치 확인
        adSettingRepository.save(setting); // DB에 저장

        // 성공 응답 반환
        return new CommonResDto(HttpStatus.OK, "광고 노출 개수 수정 완료", null);
    }

    //  광고 노출 리스트 조회
    public List<Advertisement> getAdListForDisplay() {
        // 최신 광고 노출 개수 설정값을 가져옴 (없으면 0)
        int adCount = adSettingRepository.findTopByOrderByAdNumIdDesc()
                .map(AdvertisementCount::getAdNum)
                .orElse(0);

        // 설정값이 0 이하라면 빈 리스트 반환
        if (adCount <= 0) return List.of();

        // 1차로 승인되고 활성화된 광고 전체 조회
        List<Advertisement> confirmedAds = adRepository.findByConfirmedTrueAndActiveTrue();
        List<Advertisement> result = new ArrayList<>(confirmedAds); // 결과 리스트에 추가

        // 설정된 노출 개수에서 현재 확보된 광고 수 차이 계산
        int remain = adCount - confirmedAds.size();

        // 부족한 수만큼 승인되지 않은 랜덤 광고로 채움
        if (remain > 0) {
            List<Advertisement> randomUnconfirmed = adRepository.findByConfirmedFalseRandomLimit(remain);
            result.addAll(randomUnconfirmed);
        }

        // 최종 광고 리스트 반환
        return result;
    }
}