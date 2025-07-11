package com.playdata.animalboardservice.dto.req;

import com.playdata.animalboardservice.entity.ReservationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReservationReqDto {
    @NotNull
    private ReservationStatus reservationStatus;
}
