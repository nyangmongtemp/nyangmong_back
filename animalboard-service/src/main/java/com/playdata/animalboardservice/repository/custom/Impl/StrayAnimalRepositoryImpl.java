package com.playdata.animalboardservice.repository.custom.Impl;

import com.playdata.animalboardservice.dto.SearchDto;
import com.playdata.animalboardservice.entity.StrayAnimal;
import com.playdata.animalboardservice.repository.custom.StrayAnimalRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;

import static com.playdata.animalboardservice.entity.QStrayAnimal.*;

@RequiredArgsConstructor
public class StrayAnimalRepositoryImpl implements StrayAnimalRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 유기동물 목록 조회 (검색 조건 및 페이징 처리 포함)
     * @param searchDto
     * @param pageable
     * @return
     */
    @Override
    public Page<StrayAnimal> findList(SearchDto searchDto, Pageable pageable) {
        // 조건에 맞는 유기동물 데이터 조회 (페이징 적용)
        List<StrayAnimal> list = jpaQueryFactory.select(strayAnimal)
                .from(strayAnimal)
                .where(builderCondition(searchDto))
                .offset(pageable.getOffset())       // 페이지 번호 기반 오프셋 적용
                .limit(pageable.getPageSize())      // 한 페이지 크기 제한
                .fetch();

        // 전체 데이터 개수 조회 (페이징을 위해 필요)
        Long count = 0L;
        if (!CollectionUtils.isEmpty(list)) {
            count = jpaQueryFactory.select(strayAnimal.count().coalesce(0L).as("cnt"))
                    .from(strayAnimal)
                    .where(builderCondition(searchDto))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetchOne();
        }

        // Page 객체로 변환하여 반환
        return new PageImpl<>(list, pageable, count);
    }

    // 검색 조건(QueryDSL)을 구성하는 메서드
    private BooleanBuilder builderCondition(SearchDto searchDto) {
        BooleanBuilder builder = new BooleanBuilder();

        // 보호소 주소 검색 (시/도 단위부터 검색 가능)
        if (searchDto.getCareAddr() != null && !searchDto.getCareAddr().isBlank()) {
            builder.and(strayAnimal.careAddr.startsWithIgnoreCase(searchDto.getCareAddr()));
        }

        // 축종명(개/고양이 등) 필터
        if (searchDto.getUpKindNm() != null && !searchDto.getUpKindNm().isBlank()) {
            builder.and(strayAnimal.upKindNm.eq(searchDto.getUpKindNm()));
        }

        // 성별(M,F,Q) 필터
        if (searchDto.getSexCode() != null) {
            builder.and(strayAnimal.sexCd.eq(searchDto.getSexCode()));
        }

        // 통합 검색어가 있는 경우
        if (searchDto.getSearchWord() != null && !searchDto.getSearchWord().isBlank()) {
            String keyword = searchDto.getSearchWord();
            BooleanBuilder searchBuilder = new BooleanBuilder();

            // 사용자가 입력한 검색어가 다음 필드들에 포함되는지 검사
            searchBuilder.or(strayAnimal.upKindNm.containsIgnoreCase(keyword));   // 축종명
            searchBuilder.or(strayAnimal.kindNm.containsIgnoreCase(keyword));     // 품종명
            searchBuilder.or(strayAnimal.colorCd.containsIgnoreCase(keyword));    // 색상
            searchBuilder.or(strayAnimal.age.containsIgnoreCase(keyword));        // 나이
            searchBuilder.or(strayAnimal.careNm.containsIgnoreCase(keyword));     // 보호소 이름
            searchBuilder.or(strayAnimal.careAddr.containsIgnoreCase(keyword));   // 보호소 주소
            searchBuilder.or(strayAnimal.orgNm.containsIgnoreCase(keyword));      // 관할 기관

            // 모든 조건을 하나의 BooleanBuilder에 and로 묶음
            builder.and(searchBuilder);
        }

        return builder;
    }
}
