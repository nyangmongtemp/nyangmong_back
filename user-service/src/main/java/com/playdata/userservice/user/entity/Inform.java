package com.playdata.userservice.user.entity;

import com.playdata.userservice.common.entity.BaseTimeEntity;
import com.playdata.userservice.common.enumeration.ErrorCode;
import com.playdata.userservice.common.exception.CommonException;
import com.playdata.userservice.common.util.HtmlSanitizer;
import com.playdata.userservice.user.dto.inform.res.InformListResDto;
import com.playdata.userservice.user.dto.inform.res.InformResDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
// 임의로 지정한 테이블 이름 -> 추후에 모든 서비스의 테이블 이름을 통일할 것!
@Table(name = "tbl_inform")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inform extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long informId;
    
    // 문의 작성자 (사용자)
    private Long userId;
    
    // 답변 작성자 (관리자)
    private Long adminId;
    
    // 문의 제목
    private String title;
    
    // 문의 내용
    private String content;
    
    // 삭제 여부
    private boolean active;
    
    // 답변 여부
    private boolean answered;
    
    // 답변 내용
    private String reply;

    // 문의 생성용 생성자
    public Inform(Long userId, String title, String content, HtmlSanitizer plainTextPolicy) {
        this.userId = userId;
        this.title = plainTextPolicy.sanitizeText(title);
        this.content = plainTextPolicy.sanitizeText(content);
        this.active = true;
        this.answered = false;
        this.reply = null;
        this.adminId = null;
    }

    // 문의 수정용 메소드
    public void modifyInform(String title, String content, HtmlSanitizer plainTextPolicy) {
        // 수정할 데이터가 없는 경우
        if(title == null && content == null) {
            // 에러 처리
            throw new CommonException(ErrorCode.BAD_REQUEST, "문의를 수정할 데이터가 없습니다.");
        }
        // 제목 수정
        if (title != null) {
            this.title = plainTextPolicy.sanitizeText(title);
        }
        // 내용 수정
        if (content != null) {
            this.content = plainTextPolicy.sanitizeText(content);
        }
    }

    // 문의 삭제용 메소드
    public void deleteInform() {
        this.active = false;
    }

    // 문의 상세 정보 dto 변환 메소드
    public InformResDto toDetailDto(String nickname) {
        return InformResDto.builder()
                .informId(informId)
                .userId(userId)
                .title(title)
                .content(content)
                .reply(reply)
                .adminId(adminId)
                .answered(answered)
                .nickname(nickname)
                .updateAt(this.getUpdateAt())
                .build();
    }

    // 문의 목록 조회 dto 변환 메소드
    public InformListResDto toListDto(String nickname) {
        return InformListResDto.builder()
                .informId(informId)
                .userId(userId)
                .title(title)
                .answered(answered)
                .nickname(nickname)
                .createAt(this.getCreateAt())
                .updateAt(this.getUpdateAt())
                .build();
    }

/////////////////  관리자용 메소드

    // 답변 생성
    public void makeReply(String reply, Long adminId) {
        this.reply = reply;
        this.adminId = adminId;
        this.answered = true;
    }

    // 답변 수정
    public void changeReply(String reply) {
        this.reply = reply;
        // 혹시 몰라서
        this.answered = true;
    }

}
