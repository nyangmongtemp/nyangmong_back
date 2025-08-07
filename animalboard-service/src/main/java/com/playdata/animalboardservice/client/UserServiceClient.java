package com.playdata.animalboardservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service", url = "http://user-service.default.svc.cluster.local:8000") // 호출하고자 하는 서비스 이름 (유레카에 등록된)
public interface UserServiceClient {


}
