package com.playdata.adminservice.admin.repository.custom.Impl;

import com.playdata.adminservice.admin.dto.req.UserSearchDto;
import com.playdata.adminservice.admin.entity.User;
import com.playdata.adminservice.admin.repository.custom.UserRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import static com.playdata.adminservice.admin.entity.QUser.*;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * [관리자] - 사용자 목록 조회 (검색, 페이징)
     * @param userSearchDto
     * @param pageable
     * @return
     */
    @Override
    public Page<User> findList(UserSearchDto userSearchDto, Pageable pageable) {

        List<User> list = jpaQueryFactory.select(user)
                .from(user)
                .where(builderCondition(userSearchDto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = 0L;
        if (!CollectionUtils.isEmpty(list)) {
            count = jpaQueryFactory.select(user.count().coalesce(0L).as("cnt"))
                    .from(user)
                    .where(builderCondition(userSearchDto))
                    .fetchOne();
        }

        // Page 객체로 변환하여 반환
        return new PageImpl<>(list, pageable, count);
    }

    private BooleanBuilder builderCondition(UserSearchDto userSearchDto) {
        BooleanBuilder builder = new BooleanBuilder();

        if (!StringUtils.isBlank(userSearchDto.getUsername())) {
            builder.and(user.userName.startsWithIgnoreCase(userSearchDto.getUsername()));
        }

        if (!StringUtils.isBlank(userSearchDto.getEmail())) {
            builder.and(user.email.startsWithIgnoreCase(userSearchDto.getEmail()));
        }

        if (userSearchDto.getReport() != null && userSearchDto.getReport()) {
            // pause가 true일 때만 pauseCount >= 1 조건 추가
            builder.and(user.reportCount.goe(1));
        }

        return builder;
    }
}
