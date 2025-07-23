package com.playdata.adminservice.admin.repository.custom.Impl;

import static com.playdata.adminservice.admin.entity.QAdmin.admin;
import static com.playdata.adminservice.admin.entity.QTerms.terms;

import com.playdata.adminservice.admin.dto.req.TermsSearchDto;
import com.playdata.adminservice.admin.dto.res.TermsListResDto;
import com.playdata.adminservice.admin.entity.TermsCategory;
import com.playdata.adminservice.admin.repository.custom.TermsRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class TermsRepositoryImpl implements TermsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 약관/개인정보처리방침/QNA 목록을 조건에 따라 검색하고 페이징하여 반환하는 구현체.
     *
     * @param termsCategory 약관 카테고리 (예: TERMS, POLICY, QNA 등)
     * @param searchDto 검색 조건 (제목, 작성자 이름 등을 포함)
     * @param pageable 페이지 번호, 크기, 정렬 방식 등이 포함된 페이징 객체
     * @return 조건에 맞는 약관 목록 Page 객체로 반환
     */
    @Override
    public Page<TermsListResDto> findByTermsList(TermsCategory termsCategory, TermsSearchDto searchDto, Pageable pageable) {
        // 검색 조건 및 페이징에 따라 약관 목록 조회 (작성자 이름과 조인하여 출력)
        List<TermsListResDto> list = jpaQueryFactory.select(
                        Projections.constructor(TermsListResDto.class,
                                terms.termsId,     // 약관 ID
                                terms.title,       // 제목
                                terms.content,     // 내용
                                admin.name,        // 작성자 이름 (Admin 테이블 조인)
                                terms.createAt,    // 생성일
                                terms.updateAt     // 수정일
                        ))
                .from(terms)
                .leftJoin(admin).on(terms.adminId.eq(admin.adminId)) // 작성자와 조인
                .where(
                        builderCondition(searchDto),          // 검색 조건
                        terms.active.eq(true),                // 활성화된 데이터만 조회
                        terms.category.eq(termsCategory)      // 지정된 카테고리만 조회
                )
                .offset(pageable.getOffset())      // 시작 위치 (페이징)
                .limit(pageable.getPageSize())     // 한 페이지에 조회할 수
                .fetch();                          // 결과 조회

        // 전체 결과 수를 별도로 카운트
        Long count = jpaQueryFactory
                .select(terms.count())
                .from(terms)
                .leftJoin(admin).on(terms.adminId.eq(admin.adminId))
                .where(
                        builderCondition(searchDto),  // 동일한 검색 조건
                        terms.active.eq(true)         // 활성화 여부 필터링
                )
                .fetchOne();

        // PageImpl 객체로 페이징된 결과와 전체 개수 반환
        return new PageImpl<>(list, pageable, count == null ? 0L : count);
    }

    /**
     * 검색어가 포함된 검색 조건을 생성하는 헬퍼 메서드
     *
     * @param searchDto 검색 DTO
     * @return BooleanBuilder 조건
     */
    private BooleanBuilder builderCondition(TermsSearchDto searchDto) {
        BooleanBuilder builder = new BooleanBuilder();

        if (searchDto.getSearchWord() != null && !searchDto.getSearchWord().isBlank()) {
            String keyword = searchDto.getSearchWord();
            BooleanBuilder searchBuilder = new BooleanBuilder();

            // 제목 또는 작성자 이름에 검색어가 포함된 경우
            searchBuilder.or(terms.title.containsIgnoreCase(keyword));
            searchBuilder.or(admin.name.containsIgnoreCase(keyword));

            builder.and(searchBuilder);
        }

        return builder;
    }
}
