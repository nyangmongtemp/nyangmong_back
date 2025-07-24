package com.playdata.adminservice.admin.repository.custom.Impl;

import static com.playdata.adminservice.admin.entity.QAdmin.admin;
import static com.playdata.adminservice.admin.entity.QAdminLog.adminLog;
import static com.playdata.adminservice.admin.entity.QTerms.terms;
import static com.playdata.adminservice.admin.entity.QUser.user;

import com.playdata.adminservice.admin.dto.req.AdminLogSearchDto;
import com.playdata.adminservice.admin.dto.res.AdminLogListResDto;
import com.playdata.adminservice.admin.repository.custom.AdminLogRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class AdminLogRepositoryImpl implements AdminLogRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 관리자 로그 목록 조회 구현체
     *
     * @param searchDto - 검색 조건 DTO
     * @param pageable - 페이징 정보
     * @return 페이징된 관리자 로그 목록
     */
    @Override
    public Page<AdminLogListResDto> findByAdminLogList(AdminLogSearchDto searchDto, Pageable pageable) {

        List<AdminLogListResDto> list = jpaQueryFactory.select(
                Projections.constructor(AdminLogListResDto.class,
                        adminLog.logId,
                        adminLog.adminId,
                        adminLog.userId,
                        adminLog.adminIp,
                        user.userName,
                        user.email,
                        user.nickname,
                        admin.name,
                        adminLog.createAt
                ))
                .from(adminLog)
                .leftJoin(admin).on(adminLog.adminId.eq(admin.adminId))
                .leftJoin(user).on(adminLog.userId.eq(user.userId))
                .where(builderCondition(searchDto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(adminLog.count())
                .from(adminLog)
                .leftJoin(admin).on(adminLog.adminId.eq(admin.adminId))
                .leftJoin(user).on(adminLog.userId.eq(user.userId))
                .where(builderCondition(searchDto))
                .fetchOne();

        return new PageImpl<>(list, pageable, count == null ? 0L : count);
    }

    /**
     * 검색 조건을 빌드하는 헬퍼 메서드
     *
     * @param searchDto - 검색어가 포함된 DTO
     * @return BooleanBuilder 형태의 동적 쿼리 조건
     */
    private BooleanBuilder builderCondition(AdminLogSearchDto searchDto) {
        BooleanBuilder builder = new BooleanBuilder();

        if (searchDto.getSearchWord() != null && !searchDto.getSearchWord().isBlank()) {
            String keyword = searchDto.getSearchWord();
            BooleanBuilder searchBuilder = new BooleanBuilder();

            // 제목 또는 작성자 이름에 검색어가 포함된 경우
            searchBuilder.or(admin.name.containsIgnoreCase(keyword));
            searchBuilder.or(user.userName.containsIgnoreCase(keyword));
            searchBuilder.or(user.nickname.containsIgnoreCase(keyword));
            searchBuilder.or(user.email.containsIgnoreCase(keyword));

            builder.and(searchBuilder);
        }

        return builder;
    }
}
