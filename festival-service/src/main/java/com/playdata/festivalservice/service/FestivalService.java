package com.playdata.festivalservice.service;

import com.playdata.festivalservice.dto.FestivalResponseDto;
import com.playdata.festivalservice.dto.FestivalSearchDto;
import com.playdata.festivalservice.entity.FestivalEntity;
import com.playdata.festivalservice.repository.FestivalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FestivalService {

    private final FestivalRepository festivalRepository;

    public Page<FestivalResponseDto> findFestivalList(FestivalSearchDto festivalSearchDto, Pageable pageable) {
        Page<FestivalEntity> festivalList = festivalRepository.findList(festivalSearchDto, pageable);
        return festivalList.map(e -> FestivalResponseDto.fromEntityBuilder().festivalEntity(e).build());
    }

}