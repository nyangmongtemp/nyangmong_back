package com.playdata.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "board-service")
public interface BoardServiceClient {

    @DeleteMapping("/board/deleteUser/{id}")
    ResponseEntity<?> deleteUser(@PathVariable("id") Long userId);

    @PutMapping("/board/modifyNickname/{id}/{nickname}")
    ResponseEntity<?> modifyNickname(@PathVariable("id") Long id, @PathVariable("nickname") String nickname);
}
