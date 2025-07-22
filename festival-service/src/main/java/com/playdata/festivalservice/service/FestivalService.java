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

import java.util.List;
import java.util.stream.Collectors;

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
    
    // 지도에서 사용할 모든 행사 정보를 화면단으로 넘기는 메소드
    // 추후에는
    public List<FestivalResponseDto> findAllFestivals() {

        List<FestivalEntity> all = festivalRepository.findAll();
        if(all.isEmpty()) {
            return null;
        }
        return all.stream().map(e -> FestivalResponseDto
                .fromEntityBuilder().festivalEntity(e).build()).collect(Collectors.toList());
    }
}