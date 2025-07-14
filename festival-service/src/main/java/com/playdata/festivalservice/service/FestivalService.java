package com.playdata.festivalservice.service;

import com.playdata.festivalservice.dto.FestivalResponseDto;
import com.playdata.festivalservice.entity.FestivalEntity;
import com.playdata.festivalservice.repository.FestivalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FestivalService {

    private final FestivalRepository festivalRepository;

    /**
     * 모든 축제 정보를 응답 DTO로 반환
     */
    @Transactional(readOnly = true)
    public List<FestivalResponseDto> getAllFestivals() {
        return festivalRepository.findAll().stream()
                .map(FestivalResponseDto::from)
                .collect(Collectors.toList());  //.collect 밑줄
    }

    /**
     * 단일 축제 조회 (Optional 반환)
     */
    @Transactional(readOnly = true)
    public Optional<FestivalResponseDto> getFestivalById(Long id) {
        return festivalRepository.findById(id)
                .map(FestivalResponseDto::from); //.map 밑줄
    }

    /**
     * 축제 저장 (중복 해시 체크 포함)
     */
    @Transactional
    public FestivalEntity saveFestival(FestivalEntity entity) {
        // 이미 존재하는 해시인지 확인
        boolean exists = festivalRepository.findByHash(entity.getHash()).isPresent();
        if (exists) {
            throw new IllegalArgumentException("중복된 축제 데이터입니다.");
        }
        return festivalRepository.save(entity);
    }

    /**
     * 축제 업데이트
     */
    @Transactional
    public FestivalEntity updateFestival(Long id, FestivalEntity updateData) {
        FestivalEntity existing = festivalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("축제가 존재하지 않습니다."));

        existing.updateFrom(updateData);
        return festivalRepository.save(existing);
    }

    /**
     * 축제 삭제
     */
    @Transactional
    public void deleteFestival(Long id) {
        festivalRepository.deleteById(id);
    }
}