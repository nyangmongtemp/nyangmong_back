package com.playdata.mapservice.map.controller;

import com.playdata.mapservice.common.dto.CommonResDto;
import com.playdata.mapservice.map.dto.map.req.MapSearchReqDto;
import com.playdata.mapservice.map.service.MapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
@Slf4j
public class MapController {

    private final MapService mapService;

    // 지역 또는 카테고리를 받아서 해당 객체들을 리턴
    @PostMapping("/find")
    public ResponseEntity<?> findMap(@RequestBody MapSearchReqDto reqDto){
        CommonResDto resDto = mapService.findMapList(reqDto);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // 특정 아이템의 상세 정보를 리턴
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> findMapDetail(@PathVariable(name = "id") Long mapId){

        CommonResDto resDto = mapService.findMapDetail(mapId);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

}
