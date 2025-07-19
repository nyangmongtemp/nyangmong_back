package com.playdata.mapservice.map.service;

import com.playdata.mapservice.common.dto.CommonResDto;
import com.playdata.mapservice.common.enumeration.ErrorCode;
import com.playdata.mapservice.common.exception.CommonException;
import com.playdata.mapservice.map.dto.hospital.res.HospitalDetailResDto;
import com.playdata.mapservice.map.dto.hospital.res.HospitalListDto;
import com.playdata.mapservice.map.entity.AnimalHospital;
import com.playdata.mapservice.map.entity.HospitalAddressCode;
import com.playdata.mapservice.map.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    public CommonResDto findHospitalList(String addressCode) {

        HospitalAddressCode region = HospitalAddressCode.from(addressCode);

        log.error(region.getDesc());

        List<HospitalListDto> collect = hospitalRepository.findByRegion(region.getDesc())
                .stream().map(AnimalHospital::listInfoFromEntity).collect(Collectors.toList());

        return new CommonResDto(HttpStatus.OK, "해당 지역의 모든 동물병원 찾음", collect);
    }

    public CommonResDto findHospitalDetail(Long hospitalId) {

        HospitalDetailResDto res = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new CommonException(ErrorCode.BAD_REQUEST)).detailInfoFromEntity();

        return new CommonResDto(HttpStatus.OK, "해당 병원의 상세 정보 찾음", res);
    }
}
