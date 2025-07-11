package com.playdata.animalboardservice.dto.res;

import com.playdata.animalboardservice.common.entity.BaseTimeEntity;
import com.playdata.animalboardservice.entity.Animal;
import com.playdata.animalboardservice.entity.NeuterYn;
import com.playdata.animalboardservice.entity.ReservationStatus;
import com.playdata.animalboardservice.entity.SexCode;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AnimalDetailResDto extends BaseTimeEntity {

    private Long postId; //pk 게시판 번호
    private Long userId; // 사용자 id 값
    private String thumbnailImage; // 썸넹일 이미지
    private String nickName; // 닉네임
    private String title; // 제목
    private String content; // 내용
    private int viewCount; // 조회수
    private String petCategory; // 강아지, 고양이, 기타
    private String petKind; // 세부 분류 ( 믹스종 등)
    private String age; // 나이
    private String vaccine; // 백신접종여부
    private SexCode sexCode; // 성별
    private NeuterYn neuterYn; // 중성화여부
    private String address; // 주소
    private Integer fee; // 책임비
    private boolean active; // 게시물 활성화
    private ReservationStatus reservationStatus; // 분양상태
    private LocalDateTime createAt; // 생성 일시 (자동 관리)

    @Builder
    public AnimalDetailResDto(Animal animal) {
        this.postId = animal.getPostId();
        this.userId = animal.getUserId();
        this.thumbnailImage = animal.getThumbnailImage();
        this.nickName = animal.getNickname();
        this.title = animal.getTitle();
        this.content = animal.getContent();
        this.viewCount = animal.getViewCount();
        this.petCategory = animal.getPetCategory();
        this.petKind = animal.getPetKind();
        this.age = animal.getAge();
        this.vaccine = animal.getVaccine();
        this.sexCode = animal.getSexCode();
        this.neuterYn = animal.getNeuterYn();
        this.address = animal.getAddress();
        this.fee = animal.getFee();
        this.active = animal.isActive();
        this.reservationStatus = animal.getReservationStatus();
        this.createAt = animal.getCreateAt();
    }
}
