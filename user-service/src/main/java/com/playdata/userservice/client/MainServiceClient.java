package com.playdata.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "main-service", url = "http://main-service.default.svc.cluster.local:8084")
public interface MainServiceClient {

    @DeleteMapping("/main/deleteUser/{id}")
    ResponseEntity<?> deleteUser(@PathVariable("id") Long userId);

    @PutMapping("/main/modifyNickname/{id}/{nickname}")
    ResponseEntity<?> modifyNickname(@PathVariable("id") Long id, @PathVariable("nickname") String nickname);

    @PutMapping("/main/modifyProfileImage")
    ResponseEntity<?> modifyProfileImage(@RequestParam(value = "id") Long id,
                                         @RequestParam(value = "profileImage") String profileImage);
}
