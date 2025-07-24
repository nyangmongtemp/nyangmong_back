package com.playdata.adminservice.admin.controller;

import com.playdata.adminservice.admin.dto.banner.req.BannerModiReqDto;
import com.playdata.adminservice.admin.dto.banner.req.BannerSaveReqDto;
import com.playdata.adminservice.admin.dto.banner.req.OrderModiReqDto;
import com.playdata.adminservice.admin.service.BannerService;
import com.playdata.adminservice.common.auth.TokenUserInfo;
import com.playdata.adminservice.common.dto.CommonResDto;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import com.playdata.adminservice.common.exception.CommonException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/banner")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasAnyRole('BOSS', 'CONTENT')")
public class BannerController {

    private final BannerService bannerService;
    
    // 배너 생성
    @PostMapping("/create")
    public ResponseEntity<?> createBanner(@AuthenticationPrincipal TokenUserInfo userInfo,
                                          @RequestPart(value = "banner") @Valid BannerSaveReqDto reqDto,
                                          @RequestPart(value = "thumbnailImage", required = true) MultipartFile thumbnailImage) {
        CommonResDto resDto = bannerService.createBanner(userInfo.getAdminId(), reqDto, thumbnailImage);

        return new ResponseEntity<>(resDto, HttpStatus.CREATED);
    }
    
    // 배너 상세 수정 -> 제목, 이미지
    @PatchMapping("/modify")
    public ResponseEntity<?> updateBanner(@AuthenticationPrincipal TokenUserInfo userInfo,
                                          @RequestPart(value = "banner") @Valid BannerModiReqDto reqDto,
                                          @RequestPart(value = "thumbnailImage", required = false) MultipartFile thumbnailImage) {
        // 수정할 데이터가 없는 경우
        if(reqDto.getTitle() == null && thumbnailImage == null) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "수정할 데이터가 없습니다.");
        }

        CommonResDto resDto = bannerService.updateBanner(userInfo.getAdminId(), reqDto, thumbnailImage);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
    
    // 배너 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBanner(@AuthenticationPrincipal TokenUserInfo userInfo,
                                          @PathVariable(name = "id") Long bannerId) {
        CommonResDto resDto = bannerService.deleteBanner(userInfo.getAdminId(), bannerId);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
    
    // 배너의 순서 수정
    @PatchMapping("/order")
    public ResponseEntity<?> changeOrders(@AuthenticationPrincipal TokenUserInfo userInfo,
                                          @RequestBody @Valid List<OrderModiReqDto> reqDtoList) {

        CommonResDto resDto = bannerService.updateOrder(userInfo.getAdminId(), reqDtoList);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
    
    // 배너 노출 등록
    @PatchMapping("/enroll/{id}")
    public ResponseEntity<?> enrollOrder(@PathVariable(name = "id") Long bannerId) {
        CommonResDto resDto = bannerService.enrollOrder(bannerId);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 배너 노출 해제
    @PatchMapping("/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable(name = "id") Long bannerId) {
        CommonResDto resDto = bannerService.cancelOrder(bannerId);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 노출 배너 목록 조회
    @GetMapping("/list")
    public ResponseEntity<?> getExposedList() {
        CommonResDto resDto = bannerService.getExposedList();

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 배너 상세 조회
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetail(@PathVariable(name = "id") Long bannerId) {
        CommonResDto resDto = bannerService.findDetail(bannerId);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 제목으로 검색 및 페이징
    // keyword가 없으면, 검색어 없이 조회함.
    @GetMapping("/search")
    public ResponseEntity<?> getUnorderedList(@RequestParam(value = "keyword", required = false) String keyword,
                                              @RequestParam(value = "page", defaultValue = "0") int page) {
        CommonResDto resDto = bannerService.getUnorderedList(keyword, page);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 노출할 배너 개수 수정 -> 노출 배너는 3개 이상 -> 기본 배너 + 추가 가능하게
    @PatchMapping("/count/{count}")
    public ResponseEntity<?> updateLimit(@PathVariable(name = "count") @Min(3) Integer count) {
        CommonResDto resDto = bannerService.updateLimit(count);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<?> getLimit() {
        return new ResponseEntity<>(bannerService.getLimit(), HttpStatus.OK);
    }

}
