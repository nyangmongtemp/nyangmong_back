package com.playdata.mapservice.map.controller;

import com.playdata.mapservice.common.dto.CommonResDto;
import com.playdata.mapservice.map.service.HospitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hospital")
@RequiredArgsConstructor
@Slf4j
public class HospitalController {

    private final HospitalService hospitalService;

    // 지역을 받아서 해당 지역의 모든 동물병원 목록을 리턴해주는 메소드
    @GetMapping("/list/{addressCode}")
    public ResponseEntity<CommonResDto> hospitalList(@PathVariable String addressCode) {
        CommonResDto resDto = hospitalService.findHospitalList(addressCode);

        return new ResponseEntity<CommonResDto>(resDto, HttpStatus.OK);
    }

    // id값을 통해 특정 동물병원의 상세 정보를 리턴해주는 메소드
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> hospitalDetail(@PathVariable(name = "id") Long hospitalId) {
        CommonResDto resDto = hospitalService.findHospitalDetail(hospitalId);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }


}
