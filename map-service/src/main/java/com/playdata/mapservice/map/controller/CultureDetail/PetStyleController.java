package com.playdata.mapservice.map.controller.CultureDetail;

import com.playdata.mapservice.common.dto.CommonResDto;
import com.playdata.mapservice.map.service.CultureDetail.PetStyleSejongService;
import com.playdata.mapservice.map.service.CultureDetail.PetStyleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/culture")
@RequiredArgsConstructor
@Slf4j
public class PetStyleController {

    private final PetStyleService petStyleService;
    private final PetStyleSejongService petStyleSejongService;

    // 지역 정보를 받아서 세부 지역 정보 리스트를 리턴해주는 메소드
    @GetMapping("/region/list/{addressCode}/{category}")
    public ResponseEntity<?> getRegionDetailList(@PathVariable String addressCode, @PathVariable String category) {
        CommonResDto resDto;

        if(addressCode.equals("8")) {
            resDto = petStyleSejongService.findRegionDetail(addressCode, category);
        }
        else {
            resDto = petStyleService.findRegionDetail(addressCode, category);
        }

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // sigungu값까지 받아서 해당 지역의 모든 미용실 정보를 리턴해주는 메소드
    @GetMapping("/list/{addressCode}/{sigungu}/{category}")
    public ResponseEntity<?> getList(@PathVariable String addressCode, @PathVariable String sigungu
            , @PathVariable String category) {
        CommonResDto resDto;
        if(addressCode.equals("8")) {
            resDto = petStyleSejongService.findPetStyleList(addressCode, sigungu, category);
        }
        else {
            resDto = petStyleService.findPetStyleList(addressCode, sigungu, category);
        }

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    // id값을 받아서 특정 미용실의 상세 정보를 리턴해주는 메소드
    @GetMapping("/detail/{category}/{id}")
    public ResponseEntity<?> getDetail(@PathVariable Long id ,@PathVariable String category) {
        CommonResDto resDto = petStyleService.findDetail(id, category);

        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }


}
