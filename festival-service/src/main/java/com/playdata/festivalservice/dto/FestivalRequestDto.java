package com.playdata.festivalservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FestivalRequestDto {
    private String source;
    private String title;
    private String url;
    private String location;
    private String festivalDate;
    private String festivalTime;
    private String money;
    private String reservationDate;
}