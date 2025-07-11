package com.playdata.animalboardservice.entity;

import com.playdata.animalboardservice.common.entity.BaseTimeEntity;
import com.playdata.animalboardservice.dto.req.AnimalUpdateRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "animal")
@EntityListeners(AuditingEntityListener.class)
public class Animal extends BaseTimeEntity {

    @Id
    @Column(name = "post_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId; //pk 게시판 번호

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 id 값

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "thumbnail_image", nullable = false)
    private String thumbnailImage; // 썸넹일 이미지

    @Column(name = "title", nullable = false)
    private String title; // 제목

    @Column(name = "content", nullable = false)
    private String content; // 내용

    @Column(name = "view_count")
    private int viewCount; // 조회수

    @Column(name = "pet_category", nullable = false)
    private String petCategory; // 강아지, 고양이, 기타

    @Column(name = "pet_kind", nullable = false)
    private String petKind; // 세부 분류 ( 믹스종 등 )

    @Column(name = "age", nullable = false)
    private String age; // 나이

    @Column(name = "vaccine")
    private String vaccine; // 백신접종여부

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private SexCode sexCode; // 성별

    @Enumerated(EnumType.STRING)
    @Column(name = "neuter", nullable = false)
    private NeuterYn neuterYn; // 중성화여부

    @Column(name = "address", nullable = false)
    private String address; // 주소

    @Column(name = "fee", nullable = false)
    private Integer fee; // 책임비

    @Column(name = "active")
    private boolean active; // 게시물 활성화

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status")
    private ReservationStatus reservationStatus; // 예약

    // 디폴트
    @PrePersist
    protected void onCreate() {
        this.viewCount = 0;
        this.active = true;
        this.reservationStatus = ReservationStatus.A;
    }

    // 조회수 증가
    public void viewCountUp(int viewCount) {
        this.viewCount = viewCount;
    }

    // 업데이트
    public void updateAnimal(AnimalUpdateRequestDto animalRequestDto, String newThumbnailImage) {
        this.thumbnailImage = newThumbnailImage;
        this.title = animalRequestDto.getTitle();
        this.content = animalRequestDto.getContent();
        this.petCategory = animalRequestDto.getPetCategory();
        this.petKind = animalRequestDto.getPetKind();
        this.age = animalRequestDto.getAge();
        this.vaccine = animalRequestDto.getVaccine();
        this.sexCode = animalRequestDto.getSexCode();
        this.neuterYn = animalRequestDto.getNeuterYn();
        this.address = animalRequestDto.getAddress();
        this.fee = animalRequestDto.getFee();
    }

    // 삭제
    public void deleteAnimal() {
        this.active = false;
    }

    // 예약 상태 변경
    public void reservationStatusAnimal(ReservationStatus reservationReqDto) {
        this.reservationStatus = reservationReqDto;
    }

    // 닉네임 변경
    public void changeNickname(String nickname) {
        this.nickname = URLDecoder.decode(nickname, StandardCharsets.UTF_8);
    }

}
