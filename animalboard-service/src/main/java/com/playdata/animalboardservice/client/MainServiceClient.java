package com.playdata.animalboardservice.client;

import com.playdata.animalboardservice.dto.req.LikeComCountReqDto;
import com.playdata.animalboardservice.dto.res.LikeComCountResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "main-service", url = "http://main-service.default.svc.cluster.local:8000") // 호출하고자 하는 서비스 이름 (유레카에 등록된)
public interface MainServiceClient {

    @PostMapping("/main/list")
    List<LikeComCountResDto> getListLikeCommentCount(@RequestBody List<LikeComCountReqDto> contentList);
}
