package com.playdata.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "main-service")
public interface MainServiceClient {

    @DeleteMapping("/main/deleteUser/{id}")
    ResponseEntity<?> deleteUser(@PathVariable("id") Long userId);

    @PatchMapping("/main/modifyNickname/{id}/{nickname}")
    ResponseEntity<?> modifyNickname(@PathVariable("id") Long id, @PathVariable("nickname") String nickname);

}
