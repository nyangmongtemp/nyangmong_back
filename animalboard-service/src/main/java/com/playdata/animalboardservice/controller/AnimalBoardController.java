package com.playdata.animalboardservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/animal-board")
public class AnimalBoardController {

//    @GetMapping
//    public ResponseEntity<List<AnimalBoardResponse>> getAnimalBoardList() {
//
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<AnimalBoardResponse> getAnimalBoard(@PathVariable Long id) {
//
//    }
//
//    @PostMapping
//    public ResponseEntity<Long> createAnimalBoard() {
//
//    }
//
//    @PatchMapping("/{id}")
//    public ResponseEntity<Void> updateAnimalBoard() {
//
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAnimalBoard(@PathVariable Long id) {
//
//    }
}
