package com.playdata.festivalservice.dto;

import com.playdata.festivalservice.entity.FestivalEntity;
import lombok.Builder;

@Builder
public record FestivalResponseDto(
        Long festivalId,
        String title,
        String location,
        String festivalDate,
        String imagePath,
        String money,
        String url,
        String reservationDate,
        String description,
        String festivalTime
) {
    public static FestivalResponseDto from(FestivalEntity e) {
        return FestivalResponseDto.builder()
                .festivalId(e.getFestivalId())
                .title(e.getTitle())
                .location(e.getLocation())
                .festivalDate(e.getFestivalDate())
                .imagePath(e.getImagePath())
                .money(e.getMoney())
                .url(e.getUrl())
                .reservationDate(e.getReservationDate())
                .description(e.getDescription())
                .festivalTime(e.getFestivalTime())
                .build();
    }


}