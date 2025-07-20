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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    
    // 시,도 + 구,시 를 통해서 해당 주소의 모든 동물 병원을 찾음
    public CommonResDto findHospitalList(String addressCode, String detailAddress) {
        
        // 시,도 값
        HospitalAddressCode region = HospitalAddressCode.from(addressCode);
        
        // 시,도 + 구, 시
        String targetAddress = region.getDesc() + " " + detailAddress;

        log.error(targetAddress);

        List<HospitalListDto> collect = hospitalRepository.findByRegion(targetAddress)
                .stream().map(AnimalHospital::listInfoFromEntity).collect(Collectors.toList());

        return new CommonResDto(HttpStatus.OK, "해당 지역의 모든 동물병원 찾음", collect);
    }
    
    // id값을 통한 특정 병원의 정보를 리턴
    public CommonResDto findHospitalDetail(Long hospitalId) {
        
        HospitalDetailResDto res = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new CommonException(ErrorCode.BAD_REQUEST)).detailInfoFromEntity();

        return new CommonResDto(HttpStatus.OK, "해당 병원의 상세 정보 찾음", res);
    }
    
    // 특정 시,도의 구,시 정보를 중복을 제거하여 추출
    public CommonResDto findRegionDetail(String addressCode) {

        List<String> result = hospitalRepository
                .findFullAddressByRegion(HospitalAddressCode.from(addressCode).getDesc())
                .stream().map(address -> {
                    // 공백 기준으로 문자열 분해
                    String[] parts = address.split("\\s+");
                    // 2개 이상으로 분해될 때만, 2번쨰 값을 리턴
                    return parts.length >= 2 ? parts[1] : null;
                })
                .filter(Objects::nonNull)   // null 제거
                .collect(Collectors.toSet()) // 중복 제거
                .stream().collect(Collectors.toList()); // 그냥 화면단에는 list로 보내는 것이 익숙해서 list 변환

        return new CommonResDto(HttpStatus.OK, "지역의 세부 지역 정보 리턴", result);
    }

}
