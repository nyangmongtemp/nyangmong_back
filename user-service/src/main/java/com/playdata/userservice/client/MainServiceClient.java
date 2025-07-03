package com.playdata.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "main-service")
public interface MainServiceClient {

    @GetMapping("/main/deleteUser/{id}")
    ResponseEntity<?> deleteUser(@PathVariable("id") Long userId);

    @GetMapping("/main/modifyNickname/{id}/{nickname}")
    ResponseEntity<?> modifyNickname(@PathVariable("id") Long id, @PathVariable("nickname") String nickname);

}
