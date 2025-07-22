package com.playdata.mapservice.map.service;

import com.playdata.mapservice.common.dto.CommonResDto;
import com.playdata.mapservice.common.enumeration.ErrorCode;
import com.playdata.mapservice.common.exception.CommonException;
import com.playdata.mapservice.map.dto.map.req.MapSearchReqDto;
import com.playdata.mapservice.map.dto.map.res.MapSearchResDto;
import com.playdata.mapservice.map.entity.AddressCode;
import com.playdata.mapservice.map.entity.ContentType;
import com.playdata.mapservice.map.entity.MapEntity;
import com.playdata.mapservice.map.repository.MapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class MapService {

    private final MapRepository mapRepository;

    public CommonResDto findMapList(MapSearchReqDto reqDto) {
        
        // 요청 카테고리 추출
        ContentType contentType = ContentType.from(reqDto.getContentType());
        // 요청 주소 추출
        AddressCode addressCode = AddressCode.from(reqDto.getRegion());
        // 주소와 카테고리로 해당하는 객체 전체 조회
        List<MapSearchResDto> result = mapRepository.findByTypeAndRegionList(contentType, addressCode).stream()
                .map(MapEntity::fromEntityToListDto).collect(Collectors.toList());

        return new CommonResDto(HttpStatus.OK, "모든 맵 정보의 리스트 찾음", result);
    }

    public CommonResDto findMapDetail(Long mapId) {
        
        // id 값으로 해당하는 특정 map 정보 추출
        MapEntity foundMap
                = mapRepository.findByMapId(mapId)
                .orElseThrow(() -> new CommonException(ErrorCode.BAD_REQUEST));

        return new CommonResDto(HttpStatus.OK, "맵의 상세 정보 리스트 찾음", foundMap);
    }
}
