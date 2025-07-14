package com.playdata.festivalservice.service;

import com.playdata.festivalservice.dto.FestivalResponseDto;
import com.playdata.festivalservice.dto.FestivalSearchDto;
import com.playdata.festivalservice.entity.FestivalEntity;
import com.playdata.festivalservice.repository.FestivalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FestivalService {

    private final FestivalRepository festivalRepository;

    public Page<FestivalResponseDto> findFestivalList(FestivalSearchDto festivalSearchDto, Pageable pageable) {
        log.info(">>> 요청된 searchDate = {}", festivalSearchDto.getSearchDate());

        //  날짜 조건이 없으면 그대로 DB에서 조회 (QueryDSL)
        if (festivalSearchDto.getSearchDate() == null) {
            Page<FestivalEntity> festivalList = festivalRepository.findList(festivalSearchDto, pageable);
            return festivalList.map(e -> FestivalResponseDto.fromEntityBuilder().festivalEntity(e).build());
        }

        //  날짜 조건이 있을 경우 전체 데이터 조회 후 자바에서 필터링
        List<FestivalEntity> all = festivalRepository.findAll();

        List<FestivalEntity> filtered = all.stream()
                .filter(festival -> {
                    String dateStr = festival.getFestivalDate(); // 예: "2025.06.17. (화) ~ 2025.07.31. (목)"


                    if (dateStr == null || !dateStr.contains("~")) return false;

                    try {
                        // 문자열을 "~" 기준으로 분리
                        String[] parts = dateStr.split("~");
                        if (parts.length < 2) return false;

                        // 특수문자 및 요일 제거하고 yyyy-MM-dd 형식으로 변환
                        String startStr = parts[0].replaceAll("[^0-9]", ""); // 20250704
                        String endStr = parts[1].replaceAll("[^0-9]", "");   // 20250706

                        // yyyyMMdd -> yyyy-MM-dd 로 포맷 변환
                        startStr = startStr.substring(0,4) + "-" + startStr.substring(4,6) + "-" + startStr.substring(6,8);
                        endStr = endStr.substring(0,4) + "-" + endStr.substring(4,6) + "-" + endStr.substring(6,8);

                        LocalDate start = LocalDate.parse(startStr);
                        LocalDate end = LocalDate.parse(endStr);
                        LocalDate target = festivalSearchDto.getSearchDate();



                        // target 날짜가 범위 내에 포함되는지 확인
                        return !target.isBefore(start) && !target.isAfter(end);
                    } catch (Exception e) {

                        return false;
                    }
                })
                .collect(Collectors.toList());

        // ✅ 수동 페이징 처리
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());
        List<FestivalEntity> pageContent = filtered.subList(start, end);

        return new PageImpl<>(
                pageContent.stream()
                        .map(e -> FestivalResponseDto.fromEntityBuilder().festivalEntity(e).build())
                        .collect(Collectors.toList()),
                pageable,
                filtered.size()
        );
    }

}