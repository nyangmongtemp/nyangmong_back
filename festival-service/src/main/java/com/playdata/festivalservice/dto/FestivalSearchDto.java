package com.playdata.festivalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FestivalSearchDto {
    //통합 검색어 ( 제목 등 )
    private String searchWord;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate searchDate;
}
