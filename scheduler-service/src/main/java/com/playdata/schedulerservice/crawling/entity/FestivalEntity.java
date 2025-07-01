package com.playdata.schedulerservice.crawling.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter @ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "festival")
public class FestivalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long festivalId;
    private String source;
    private String title;
    private String url;
    private String location;
    private String festivalDate;
    private String festivalTime;
    private String money;
    private String imagePath;
    private String reservationDate;
    @Column(nullable = false, unique = true)
    private String hash;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void updateFrom(FestivalEntity sourceEvent) {
        this.source = sourceEvent.getSource();
        this.title = sourceEvent.getTitle();
        this.url = sourceEvent.getUrl();
        this.location = sourceEvent.getLocation();
        this.festivalDate = sourceEvent.getFestivalDate();
        this.festivalTime = sourceEvent.getFestivalTime();
        this.money = sourceEvent.getMoney();
        this.imagePath = sourceEvent.getImagePath();
        this.reservationDate = sourceEvent.getReservationDate();
    }
}
