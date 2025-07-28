package com.playdata.adminservice.admin.repository.custom.Impl;

import com.playdata.adminservice.admin.dto.AdminSearchDto;
import com.playdata.adminservice.admin.entity.Admin;
import com.playdata.adminservice.admin.entity.Role;
import com.playdata.adminservice.admin.repository.custom.AdminRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import java.util.List;

import static com.playdata.adminservice.admin.entity.QAdmin.admin;

@RequiredArgsConstructor
public class AdminRepositoryImpl implements AdminRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Admin> findList(AdminSearchDto adminSearchDto, Pageable pageable) {
        // 관리자 목록 조회 (조건 + 페이징 적용)
        List<Admin> list = jpaQueryFactory.selectFrom(admin) // 여기있는 내용 다 보겠다
                .where(buildCondition(adminSearchDto)) // 동적 검색 조건
                .offset(pageable.getOffset()) // 시작 위치 , 페이지 수
                .limit(pageable.getPageSize()) // 가져올 개수 , 설정 해 놓은 한페이지에 가져올 수
                .fetch();

        // 전체 데이터 개수 조회 (페이징을 위해 필요)
        Long count = 0L;
        if (!CollectionUtils.isEmpty(list)) {
            count = jpaQueryFactory.select(admin.count().coalesce(0L).as("cnt"))
                    .from(admin)
                    .where(buildCondition(adminSearchDto))
                    .fetchOne();
        }

        // Page 객체로 리턴
        return new PageImpl<>(list, pageable, count);
    }

    private BooleanBuilder buildCondition(AdminSearchDto adminSearchDto) {
        BooleanBuilder builder = new BooleanBuilder();

        // 관리자 권한 검색 조건
        if (adminSearchDto.getRole() != null) { // 권한이 null 이 아니라면 (권한이 선택 되었다면)
            // admin 테이블의 role 컬럼이 해당 값과 정확히 일치하는지 조건 추가
            builder.and(admin.role.eq(adminSearchDto.getRole()));
        }

        // 관리자 이름 검색 조건
        if (!StringUtils.isBlank(adminSearchDto.getName())) {
            builder.and(admin.name.startsWithIgnoreCase(adminSearchDto.getName()));
        }

        // 관리자 활성 상태
        if (adminSearchDto.getActive() != null) {
            builder.and(admin.active.eq(adminSearchDto.getActive()));
        }

        return builder;
    }
}
