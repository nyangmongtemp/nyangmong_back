package com.playdata.boardservice.client;

import com.playdata.boardservice.board.dto.LikeComCountResDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "main-service") // 호출하고자 하는 서비스 이름 (유레카에 등록된)
public interface MainServiceClient {

    @GetMapping("/main/introduction")
    List<LikeComCountResDto> getMainIntroduction();
}
