package com.playdata.boardservice.client;

import com.playdata.boardservice.board.dto.req.LikeComCountReqDto;
import com.playdata.boardservice.board.dto.res.LikeComCountResDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "main-service", url = "http://main-service.default.svc.cluster.local:8000") // 호출하고자 하는 서비스 이름 (유레카에 등록된)
public interface MainServiceClient {

    @GetMapping("/main/introduction")
    List<LikeComCountResDto> getMainIntroduction();

    @PostMapping("/main/list")
    List<LikeComCountResDto> getListLikeCommentCount(@RequestBody List<LikeComCountReqDto> contentList);
}
