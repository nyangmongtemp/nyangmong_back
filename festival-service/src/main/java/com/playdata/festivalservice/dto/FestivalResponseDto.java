package com.playdata.festivalservice.dto;

import com.playdata.festivalservice.entity.FestivalEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FestivalResponseDto {
    Long festivalId;
    String title;
    String location;
    String festivalDate;
    String imagePath;
    String money;
    String url;
    String reservationDate;
    String description;
    String festivalTime;

    @Builder(builderMethodName = "fromEntityBuilder")
    public FestivalResponseDto(FestivalEntity festivalEntity) {
        this.festivalId = festivalEntity.getFestivalId();
        this.title = festivalEntity.getTitle();
        this.location = festivalEntity.getLocation();
        this.festivalDate = festivalEntity.getFestivalDate();
        this.imagePath = festivalEntity.getImagePath();
        this.money = festivalEntity.getMoney();
        this.url = festivalEntity.getUrl();
        this.reservationDate = festivalEntity.getReservationDate();
        this.description = festivalEntity.getDescription();
        this.festivalTime = festivalEntity.getFestivalTime();
    }
}
