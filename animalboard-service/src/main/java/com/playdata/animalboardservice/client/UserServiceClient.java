package com.playdata.animalboardservice.client;

import com.playdata.animalboardservice.common.auth.TokenUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service") // 호출하고자 하는 서비스 이름 (유레카에 등록된)
public interface UserServiceClient {


}
