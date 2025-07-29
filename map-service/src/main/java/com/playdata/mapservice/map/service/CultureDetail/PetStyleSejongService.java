package com.playdata.mapservice.map.service.CultureDetail;

import com.playdata.mapservice.common.dto.CommonResDto;
import com.playdata.mapservice.common.enumeration.ErrorCode;
import com.playdata.mapservice.common.exception.CommonException;
import com.playdata.mapservice.map.dto.CultureDetail.PetStyle.res.PetStyleListResDto;
import com.playdata.mapservice.map.entity.CultureDetail.*;
import com.playdata.mapservice.map.repository.cultureDetail.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetStyleSejongService {

    private final PetStyleRepository petStyleRepository;
    private final PetCafeRepository petCafeRepository;
    private final PetShopRepository petShopRepository;
    private final MuseumRepository museumRepository;
    private final LiteraryCenterRepository literaryCenterRepository;
    private final DrugStoreRepository drugStoreRepository;
    private final ArtRepository artRepository;

    // 입력값 유효성 검증용
    private final List<String> categoryList = List.of("style", "cafe", "shop", "museum", "art", "literary", "drug");

    public CommonResDto findRegionDetail(String addressCode, String category) {

        if(!categoryList.contains(category)) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "잘못된 카테고리 입력값입니다.");
        }

        String targetRegion = CultureAddressCode.from(addressCode).getDesc();
        List<String> detailRegion;

        if(category.equals("style")) {
            detailRegion = petStyleRepository.findDetailSejong(targetRegion);
        }
        else if (category.equals("cafe")) {
            detailRegion = petCafeRepository.findDetailSejong(targetRegion);
        }
        else if (category.equals("shop")) {
            detailRegion = petShopRepository.findDetailSejong(targetRegion);
        }
        else if (category.equals("museum")) {
            detailRegion = museumRepository.findDetailSejong(targetRegion);
        }
        else if (category.equals("art")) {
            detailRegion = artRepository.findDetailSejong(targetRegion);
        }
        else if (category.equals("literary")) {
            detailRegion = literaryCenterRepository.findDetailSejong(targetRegion);
        }
        else if(category.equals("drug")){
            detailRegion = drugStoreRepository.findDetailSejong(targetRegion);
        } else {
            throw new CommonException(ErrorCode.BAD_REQUEST, "요청 중 오류 발생");
        }

        return new CommonResDto(HttpStatus.OK, "해당 지역의 시군구 정보 찾음", detailRegion);
    }

    public CommonResDto findPetStyleList(String addressCode, String sigungu, String category) {

        if(!categoryList.contains(category)) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "잘못된 카테고리 입력값입니다.");
        }

        List<PetStyleListResDto> result;
        if(category.equals("style")) {
            result = petStyleRepository.findListByRegionSejong(CultureAddressCode.from(addressCode).getDesc(), sigungu)
                    .stream().map(PetStyle::toPetStyleListResDto).collect(Collectors.toList());
        } else if(category.equals("cafe")) {
            result = petCafeRepository.findListByRegionSejong(CultureAddressCode.from(addressCode).getDesc(), sigungu)
                    .stream().map(PetCafe::toPetStyleListResDto).collect(Collectors.toList());
        } else if(category.equals("shop")) {
            result = petShopRepository.findListByRegionSejong(CultureAddressCode.from(addressCode).getDesc(), sigungu)
                    .stream().map(PetShop::toPetStyleListResDto).collect(Collectors.toList());
        } else if (category.equals("drug")) {
            result = drugStoreRepository.findListByRegionSejong(CultureAddressCode.from(addressCode).getDesc(), sigungu)
                    .stream().map(DrugStore::toPetStyleListResDto).collect(Collectors.toList());
        } else if (category.equals("art")) {
            result = artRepository.findListByRegionSejong(CultureAddressCode.from(addressCode).getDesc(), sigungu)
                    .stream().map(Art::toPetStyleListResDto).collect(Collectors.toList());
        } else if (category.equals("literary")) {
            result = literaryCenterRepository.findListByRegionSejong(CultureAddressCode.from(addressCode).getDesc(), sigungu)
                    .stream().map(LiteraryCenter::toPetStyleListResDto).collect(Collectors.toList());
        } else if (category.equals("museum")) {
            result = museumRepository.findListByRegionSejong(CultureAddressCode.from(addressCode).getDesc(), sigungu)
                    .stream().map(Museum::toPetStyleListResDto).collect(Collectors.toList());
        } else {
            throw new CommonException(ErrorCode.BAD_REQUEST, "요청 중 오류 발생");
        }

        return new CommonResDto(HttpStatus.OK, "해당 지역의 미용실 정보 찾음", result);
    }

}
