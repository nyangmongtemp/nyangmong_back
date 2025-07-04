package com.playdata.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "main-service")
public interface MainServiceClient {

    @DeleteMapping("/main/deleteUser/{id}")
    ResponseEntity<?> deleteUser(@PathVariable("id") Long userId);

    @PutMapping("/main/modifyNickname/{id}/{nickname}")
    ResponseEntity<?> modifyNickname(@PathVariable("id") Long id, @PathVariable("nickname") String nickname);

    @PutMapping("/main/modifyProfileImage/{id}/{profileImage}")
    ResponseEntity<?> modifyProfileImage(@PathVariable("id") Long id, @PathVariable("profileImage") String profileImage);
}
