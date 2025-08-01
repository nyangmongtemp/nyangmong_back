package com.playdata.adminservice.admin.dto.board.res;

import com.playdata.adminservice.admin.entity.NeuterYn;
import com.playdata.adminservice.admin.entity.ReservationStatus;
import com.playdata.adminservice.admin.entity.SexCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AnimalListResDto {

    private Long postId; //pk 게시판 번호
    private Long userId; // 사용자 id 값
    private String thumbnailImage; // 썸넹일 이미지
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
    private ReservationStatus reservationStatus; // 예약
    private LocalDateTime createAt; // 작성일자
    private String nickname;

    @Builder
    public AnimalListResDto(Long postId, Long userId, String thumbnailImage, String title,
            String content, int viewCount, String petCategory, String petKind,
            String age, String vaccine, SexCode sexCode, NeuterYn neuterYn,
            String address, Integer fee, boolean active, ReservationStatus reservationStatus, LocalDateTime createAt, String nickname) {
        this.postId = postId;
        this.userId = userId;
        this.thumbnailImage = thumbnailImage;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.petCategory = petCategory;
        this.petKind = petKind;
        this.age = age;
        this.vaccine = vaccine;
        this.sexCode = sexCode;
        this.neuterYn = neuterYn;
        this.address = address;
        this.fee = fee;
        this.active = active;
        this.reservationStatus = reservationStatus;
        this.createAt = createAt;
        this.nickname = nickname;
    }

    public AnimalListResDto(AnimalListResDto animal, Long likeCount, Long commentCount) {
        this.postId = animal.getPostId();
        this.userId = animal.getUserId();
        this.thumbnailImage = animal.getThumbnailImage();
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
        this.reservationStatus = animal.reservationStatus;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

    // 좋아요, 댓글 개수
    private Long likeCount;
    private Long commentCount;
}
