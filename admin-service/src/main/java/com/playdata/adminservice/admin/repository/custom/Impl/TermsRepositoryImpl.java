package com.playdata.adminservice.admin.repository.custom.Impl;

import static com.playdata.adminservice.admin.entity.QAdmin.admin;
import static com.playdata.adminservice.admin.entity.QTerms.terms;

import com.playdata.adminservice.admin.dto.req.TermsSearchDto;
import com.playdata.adminservice.admin.dto.res.TermsListResDto;
import com.playdata.adminservice.admin.repository.custom.TermsRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class TermsRepositoryImpl implements TermsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 약관/개인정보처리방침/QNA 목록조회(검색, 페이징)
     *
     * @param searchDto
     * @param pageable
     * @return
     */
    @Override
    public Page<TermsListResDto> findByTermsList(TermsSearchDto searchDto, Pageable pageable) {
        List<TermsListResDto> list = jpaQueryFactory.select(
                Projections.constructor(TermsListResDto.class,
                        terms.termsId,
                        terms.title,
                        terms.content,
                        admin.name,
                        terms.createAt,
                        terms.updateAt
                ))
                .from(terms)
                .leftJoin(admin).on(terms.adminId.eq(admin.adminId))
                .where(builderCondition(searchDto), terms.active.eq(true))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(terms.count())
                .from(terms)
                .leftJoin(admin).on(terms.adminId.eq(admin.adminId))
                .where(builderCondition(searchDto), terms.active.eq(true))
                .fetchOne();

        return new PageImpl<>(list, pageable, count == null ? 0L : count);
    }

    private BooleanBuilder builderCondition(TermsSearchDto searchDto) {
        BooleanBuilder builder = new BooleanBuilder();

        if (searchDto.getSearchWord() != null && !searchDto.getSearchWord().isBlank()) {
            String keyword = searchDto.getSearchWord();
            BooleanBuilder searchBuilder = new BooleanBuilder();

            searchBuilder.or(terms.title.containsIgnoreCase(keyword));
            searchBuilder.or(admin.name.containsIgnoreCase(keyword));

            builder.and(searchBuilder);
        }

        return builder;
    }
}
