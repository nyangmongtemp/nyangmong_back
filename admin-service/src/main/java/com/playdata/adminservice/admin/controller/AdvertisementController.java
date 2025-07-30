package com.playdata.adminservice.admin.controller;
import org.springframework.web.multipart.MultipartFile;
import com.playdata.adminservice.admin.dto.req.*;
import com.playdata.adminservice.admin.dto.res.AdResDto;
import com.playdata.adminservice.admin.entity.Advertisement;
import com.playdata.adminservice.admin.service.AdvertisementService;
import com.playdata.adminservice.common.dto.CommonResDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

/**
 * 광고 관련 요청을 처리하는 컨트롤러 클래스
 */
@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('BOSS', ' CONTENT')")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    /**
     * 광고 등록 API
     * @param dto 광고 등록 요청 DTO (JSON)
     * @param image 광고 이미지 파일
     * @return 등록 결과 응답 DTO
     */
    @PostMapping(value = "/ads", consumes = "multipart/form-data")
    public ResponseEntity<CommonResDto> registerAd(
            @RequestPart("dto") @Valid AdRegisterReqDto dto,
            @RequestPart("image") MultipartFile image
    ) {
        CommonResDto resDto = advertisementService.registerAd(dto, image);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
    /**
     * 광고 수정 API
     * @param id 수정할 광고 ID
     * @param dto 광고 수정 요청 DTO (JSON)
     * @return 수정 결과 응답 DTO
     */
    @PutMapping(value = "/ads/{id}", consumes = "multipart/form-data")
    public ResponseEntity<CommonResDto> updateAd(
            @PathVariable Long id,
            @RequestPart("dto") @Valid AdUpdateReqDto dto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        CommonResDto resDto = advertisementService.updateAd(id, dto, image);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * 광고 상세 조회 API
     * @param id 조회할 광고 ID
     * @return 광고 상세 정보 응답 DTO
     */
    @GetMapping("/ads/{id}")
    public ResponseEntity<?> getAd(@PathVariable Long id) {
        CommonResDto resDto = advertisementService.getAd(id);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * 광고 검색(필터링 + 페이징) API
     * @param searchDto 검색 조건 DTO (쿼리 파라미터로 받음)
     * @param pageable 페이징 정보
     * @return 검색된 광고 목록 페이지 응답 DTO
     */
    @PostMapping("/ads/search")
    public ResponseEntity<?> getAdsList(AdSearchDto searchDto, Pageable pageable) {
        Page<AdResDto> pageResult = advertisementService.getAdsList(searchDto, pageable);
        CommonResDto resDto = new CommonResDto(HttpStatus.OK, "검색 완료", pageResult);
        return ResponseEntity.ok(resDto);
    }

    /**
     * 광고 개수(순서 등 포함) 수정 API
     * @param dto 광고 개수 수정 요청 DTO
     * @return 수정 결과 응답 DTO
     */
    @PatchMapping("/ads/count")
    public ResponseEntity<CommonResDto> updateAdCount(@RequestBody @Valid AdCountReqDto dto) {
        log.info("/ads/count: Patch, dto: {}", dto.toString());
        CommonResDto response = advertisementService.updateAdCount(dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 광고 노출용 리스트 조회 API
     * (프론트 화면에 노출될 광고 목록을 위한 API)
     * @return 광고 리스트 응답 DTO
     */
    @GetMapping("/ads/display")
    public ResponseEntity<CommonResDto> getAdsForDisplay() {
        List<Advertisement> ads = advertisementService.getAdListForDisplay();
        CommonResDto response = new CommonResDto(HttpStatus.OK, "광고 노출 리스트 조회 성공", ads);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}