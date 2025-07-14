package com.playdata.festivalservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "festival",
        indexes = {
                @Index(name = "idx_festival_title", columnList = "title"),
                @Index(name = "idx_festival_date", columnList = "festivalDate")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FestivalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long festivalId;

    @Column(nullable = false, length = 100)
    private String title;                      // 축제 이름

    @Column(length = 255)
    private String location;                   // 개최 장소

    @Column(length = 50)
    private String festivalDate;               // 축제 날짜 (예: "2025-07-15 ~ 2025-07-20")

    @Column(length = 50)
    private String festivalTime;               // 축제 시간 (예: "10:00 ~ 18:00")

    @Column(length = 255)
    private String url;                        // 공식 홈페이지 URL

    @Column(length = 500)
    private String description;                // 축제 설명

    @Column(length = 255)
    private String money;                      // 입장료 등 비용 정보

    @Column(length = 500)
    private String imagePath;                  // 이미지 저장 경로 또는 URL

    @Column(length = 50)
    private String reservationDate;            // 예약 가능 기간

    @Column(nullable = false, unique = true, length = 64)
    private String hash;                       // 중복 방지용 해시 값 (예: 축제 제목+날짜 해시)

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;           // 데이터 생성 시간

    @UpdateTimestamp
    private LocalDateTime updatedAt;           // 데이터 수정 시간

    // 축제 정보 업데이트용 메서드
    public void updateFrom(FestivalEntity source) {
        this.title = source.getTitle();
        this.location = source.getLocation();
        this.festivalDate = source.getFestivalDate();
        this.festivalTime = source.getFestivalTime();
        this.url = source.getUrl();
        this.description = source.getDescription();
        this.money = source.getMoney();
        this.imagePath = source.getImagePath();
        this.reservationDate = source.getReservationDate();
    }
}