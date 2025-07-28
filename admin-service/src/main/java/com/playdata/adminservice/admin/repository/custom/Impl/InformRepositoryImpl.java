package com.playdata.adminservice.admin.repository.custom.Impl;

import static com.playdata.adminservice.admin.entity.QUser.user;
import static com.playdata.adminservice.admin.entity.QAdmin.admin;
import static com.playdata.adminservice.admin.entity.QInform.inform;

import com.playdata.adminservice.admin.dto.req.InformSearchDto;
import com.playdata.adminservice.admin.dto.res.InformDetailResDto;
import com.playdata.adminservice.admin.dto.res.InformListResDto;
import com.playdata.adminservice.admin.repository.custom.InformRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class InformRepositoryImpl implements InformRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 검색조건과 페이징 처리된 문의 리스트를 조회
     */
    @Override
    public Page<InformListResDto> findByInformList(InformSearchDto searchDto, Pageable pageable) {
        // 검색 조건 및 페이징에 따라 약관 목록 조회 (작성자 이름과 조인하여 출력)
        List<InformListResDto> list = jpaQueryFactory.select(
                        Projections.constructor(InformListResDto.class,
                                inform.informId,     // 약관 ID
                                inform.title,        // 제목
                                inform.answered,     // 답변여부
                                inform.createAt,     // 생성일
                                user.userName,       // 사용자 이름
                                user.email           // 사용자 이메일
                        ))
                .from(inform)
                .leftJoin(user).on(inform.userId.eq(user.userId)) // 작성자와 조인
                .where(builderCondition(searchDto)) // 검색조건
                .offset(pageable.getOffset())      // 시작 위치 (페이징)
                .limit(pageable.getPageSize())     // 한 페이지에 조회할 수
                .fetch();                          // 결과 조회

        // 전체 결과 수를 별도로 카운트
        Long count = jpaQueryFactory
                .select(inform.count())
                .from(inform)
                .leftJoin(user).on(inform.userId.eq(user.userId))
                .where(builderCondition(searchDto))
                .fetchOne();

        // PageImpl 객체로 페이징된 결과와 전체 개수 반환
        return new PageImpl<>(list, pageable, count == null ? 0L : count);
    }

    /**
     * 문의 상세 조회
     */
    @Override
    public InformDetailResDto findByInform(Long id) {
        return jpaQueryFactory
                .select(Projections.constructor(InformDetailResDto.class,
                        inform.title,       // 제목
                        inform.content,     // 내용
                        inform.reply,       // 답변
                        inform.answered,    // 답변여부
                        inform.createAt,    // 등록날짜
                        user.userName,      // 사용자 이름
                        user.email,         // 사용자 이메일
                        inform.updateAt,    // 답변날짜
                        admin.name          // 관지라 이름
                ))
                .from(inform)
                .leftJoin(user).on(inform.userId.eq(user.userId))
                .leftJoin(admin).on(inform.adminId.eq(admin.adminId))
                .where(inform.informId.eq(id))
                .fetchOne();
    }

    /**
     * 검색어가 포함된 검색 조건을 생성하는 헬퍼 메서드
     *
     * @param searchDto 검색 DTO
     * @return BooleanBuilder 조건
     */
    private BooleanBuilder builderCondition(InformSearchDto searchDto) {
        BooleanBuilder builder = new BooleanBuilder();

        if (searchDto.getSearchWord() != null && !searchDto.getSearchWord().isBlank()) {
            String keyword = searchDto.getSearchWord();
            BooleanBuilder searchBuilder = new BooleanBuilder();

            // 제목 또는 작성자 이름에 검색어가 포함된 경우
            searchBuilder.or(user.email.containsIgnoreCase(keyword));
            searchBuilder.or(user.userName.containsIgnoreCase(keyword));
            searchBuilder.or(inform.title.containsIgnoreCase(keyword));

            builder.and(searchBuilder);
        }

        if (searchDto.getAnswered() != null) {
            builder.and(inform.answered.eq(searchDto.getAnswered()));
        }

        return builder;
    }
}
