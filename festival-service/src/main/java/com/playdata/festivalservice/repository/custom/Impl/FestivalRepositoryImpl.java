package com.playdata.festivalservice.repository.custom.Impl;

import com.playdata.festivalservice.dto.FestivalSearchDto;
import com.playdata.festivalservice.entity.FestivalEntity;
import com.playdata.festivalservice.entity.QFestivalEntity;
import com.playdata.festivalservice.repository.custom.FestivalRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.playdata.festivalservice.entity.QFestivalEntity.*;

@RequiredArgsConstructor
public class FestivalRepositoryImpl implements FestivalRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 축제 목록 조회 (검색 조건 및 페이징 처리 포함)
     * @param festivalSearchDto
     * @param pageable
     * @return
     */
    @Override
    public Page<FestivalEntity> findList(FestivalSearchDto festivalSearchDto, Pageable pageable) {

        // 조건에 맞는 페이징된 데이터 조회
        List<FestivalEntity> content = queryFactory
                .select(festivalEntity)
                .from(festivalEntity)
                .where(builderCondition(festivalSearchDto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        content.forEach(f -> System.out.println("festivalId: " + f.getFestivalId()));



        // 전체 데이터 개수 조회
        long count = 0L;

        if (!content.isEmpty()) {
            count = Optional.ofNullable(
                    queryFactory.select(festivalEntity.count())
                            .from(festivalEntity)
                            .where(builderCondition(festivalSearchDto))
                            .fetchOne()
            ).orElse(0L);
        }

        return new PageImpl<>(content, pageable, count);
    }

    //검색 조건 생성기
    private BooleanBuilder builderCondition(FestivalSearchDto dto) {
        QFestivalEntity festival = festivalEntity;
        BooleanBuilder builder = new BooleanBuilder();

        // searchWord가 있을 경우, 제목(title) 검색어가 포함되면 필터링
        if (dto.getSearchWord() != null && !dto.getSearchWord().isBlank()) {
            builder.and(
                    festival.title.containsIgnoreCase(dto.getSearchWord())
            );
        }

        if (dto.getSearchDate() != null) {
            builder.and(
                    festival.festivalDate.containsIgnoreCase(dto.getSearchWord())
            );
        }




        return builder;
    }


}
