package com.playdata.adminservice.admin.repository.custom.Impl;

import com.playdata.adminservice.admin.dto.req.UserSearchDto;
import com.playdata.adminservice.admin.dto.res.UserListResDto;
import com.playdata.adminservice.admin.repository.custom.UserRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.playdata.adminservice.admin.entity.QUser.*;
import static com.playdata.adminservice.admin.entity.QReport.*;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * [관리자] - 사용자 목록 조회 (검색, 페이징)
     * @param searchDto
     * @param pageable
     * @return
     */
    @Override
    public Page<UserListResDto> findList(UserSearchDto searchDto, Pageable pageable) {

        List<UserListResDto> list = jpaQueryFactory.select(
                Projections.constructor(UserListResDto.class,
                        user.userId,
                        user.userName,
                        user.email,
                        user.nickname,
                        user.active,
                        user.pauseCount,
                        user.createAt,
                        // 사용자마다 처리되지 않은(treat = false) 신고 건수(count)를 조회하는 서브쿼리입니다.
                        JPAExpressions.select(report.count().intValue())         // report 테이블에서 count(*) 결과를 Integer 타입으로 변환해 조회
                                .from(report)                                    // 신고 테이블(report)에서
                                .where(report.accusedUserId.eq(user.userId)      // 현재 user의 userId와 accusedUserId가 같은 건
                                        .and(report.treat.isFalse()))            // 그리고 신고가 아직 처리되지 않은(false) 건만 필터링
                ))
                .from(user)
                .where(builderCondition(searchDto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(user.count())
                .from(user)
                .where(builderCondition(searchDto))
                .fetchOne();

        // Page 객체로 변환하여 반환
        return new PageImpl<>(list, pageable, count == null ? 0 : count);
    }

    private BooleanBuilder builderCondition(UserSearchDto searchDto) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!StringUtils.isBlank(searchDto.getKeyword())) {
            String keyword = searchDto.getKeyword();
            BooleanBuilder searchBuilder = new BooleanBuilder();

            // 제목 또는 작성자 이름에 검색어가 포함된 경우
            searchBuilder.or(user.userName.containsIgnoreCase(keyword));
            searchBuilder.or(user.email.containsIgnoreCase(keyword));
            searchBuilder.or(user.nickname.containsIgnoreCase(keyword));

            builder.and(searchBuilder);
        }

        if (searchDto.getReport() != null && searchDto.getReport()) {
            // "신고 내역이 한 건 이상 존재하는지" 여부를 판단하기 위한 서브쿼리.
            // JPAExpressions.selectOne()는 값을 직접 조회하는 것이 아니라
            // 해당 조건에 부합하는 데이터가 존재하는지(exists)만 검사할 때 사용합니다.
            builder.and(
                    JPAExpressions.selectOne()                           // 1을 선택한다는 의미로, 값을 조회하지 않고 존재 여부만 확인
                            .from(report)                                // 신고 테이블(report)에서
                            .where(report.accusedUserId.eq(user.userId)  // 현재 userId가 신고당한 대상이고
                                    .and(report.treat.isFalse()))        // 신고가 처리되지 않은 경우
                            .exists()                                    // 해당 조건에 부합하는 row가 존재하는지 체크
            );
            // 동작 설명:
            // exists() 는 SQL의 EXISTS와 동일하게 동작합니다.
            // 즉, '신고 테이블에서 신고당한 사용자가 현재 user인, 처리되지 않은 신고가 존재하는가?'를 물어보고,
            // 존재하면 true, 없으면 false를 반환합니다.
            // 따라서 이 조건을 추가하면, "신고 내역이 하나라도 있는 사용자"만 조회 대상에 포함됩니다.
        }

        if (searchDto.getActive() != null) {
            builder.and(user.active.eq(searchDto.getActive()));
        }

        return builder;
    }
}
