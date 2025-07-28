package com.playdata.mainservice.main.service;

import com.playdata.mainservice.common.dto.CommonResDto;
import com.playdata.mainservice.main.dto.res.BannerListResDto;
import com.playdata.mainservice.main.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.playdata.mainservice.main.entity.Banner;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BannerService {

    private final BannerRepository bannerRepository;

    public CommonResDto getExposedList() {

        // 노출할 배너 조회
        List<BannerListResDto> resDto = bannerRepository.getExposedBanners().stream().map(Banner::toListDto).toList();

        return new CommonResDto(HttpStatus.OK, "노출시킬 배너 목록 조회", resDto);
    }

}
