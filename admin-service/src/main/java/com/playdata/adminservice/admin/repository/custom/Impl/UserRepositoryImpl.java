package com.playdata.adminservice.admin.repository.custom.Impl;

import com.playdata.adminservice.admin.dto.req.UserSearchDto;
import com.playdata.adminservice.admin.dto.res.UserListResDto;
import com.playdata.adminservice.admin.repository.custom.UserRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.playdata.adminservice.admin.entity.QUser.*;

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
                        user.reportCount,
                        user.pauseCount,
                        user.createAt
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

        if (!StringUtils.isBlank(searchDto.getUsername())) {
            builder.and(user.userName.startsWithIgnoreCase(searchDto.getUsername()));
        }

        if (!StringUtils.isBlank(searchDto.getEmail())) {
            builder.and(user.email.startsWithIgnoreCase(searchDto.getEmail()));
        }

        if (searchDto.getReport() != null && searchDto.getReport()) {
            // pause가 true일 때만 pauseCount >= 1 조건 추가
            builder.and(user.reportCount.goe(1));
        }

        return builder;
    }
}
