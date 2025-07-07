package com.playdata.boardservice.client;

import com.playdata.boardservice.board.dto.IntroductionLikeCountListResDto;
import com.playdata.boardservice.common.dto.CommonResDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "main-service") // 호출하고자 하는 서비스 이름 (유레카에 등록된)
public interface MainServiceClient {

    @GetMapping("/introduction")
    CommonResDto<List<IntroductionLikeCountListResDto>> getMainIntroduction();
}
