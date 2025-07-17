package com.playdata.mapservice.map.service;

import com.playdata.mapservice.common.dto.CommonResDto;
import com.playdata.mapservice.common.enumeration.ErrorCode;
import com.playdata.mapservice.common.exception.CommonException;
import com.playdata.mapservice.map.dto.req.MapSearchReqDto;
import com.playdata.mapservice.map.dto.res.MapSearchResDto;
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

        ContentType contentType = ContentType.from(reqDto.getContentType());
        log.error(contentType.toString());
        AddressCode addressCode = AddressCode.from(reqDto.getRegion());
        log.error(addressCode.toString());
        List<MapSearchResDto> result = mapRepository.findByTypeAndRegionList(contentType, addressCode).stream()
                .map(MapEntity::fromEntityToListDto).collect(Collectors.toList());

        return new CommonResDto(HttpStatus.OK, "모든 맵 정보의 리스트 찾음", result);
    }

    public CommonResDto findMapDetail(Long mapId) {

        MapEntity foundMap
                = mapRepository.findByMapId(mapId)
                .orElseThrow(() -> new CommonException(ErrorCode.BAD_REQUEST));

        return new CommonResDto(HttpStatus.OK, "맵의 상세 정보 리스트 찾음", foundMap);
    }
}
