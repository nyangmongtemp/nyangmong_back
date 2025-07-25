package com.playdata.adminservice.admin.dto.req;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminBoardReqDto {
    private String title;
    private String category;
    private Long userId;
    private Boolean active;
    private String sortBy;
}