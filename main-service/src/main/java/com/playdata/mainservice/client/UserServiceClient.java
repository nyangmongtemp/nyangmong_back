package com.playdata.mainservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://user-service.default.svc.cluster.local:8086")
public interface UserServiceClient {

    @GetMapping("/user/profileImage/{id}")
    ResponseEntity<String> getUserProfileImage(@PathVariable(name = "id") Long userId);

}
