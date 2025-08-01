package com.playdata.adminservice.admin.repository.custom.Impl;


import com.playdata.adminservice.admin.dto.board.AnimalSearchDto;
import com.playdata.adminservice.admin.dto.board.res.AnimalListResDto;
import com.playdata.adminservice.admin.repository.custom.AnimalRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.playdata.adminservice.admin.entity.QAnimal.animal;

@RequiredArgsConstructor
public class AnimalRepositoryImpl implements AnimalRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 분양동물 목록 조회 (검색 조건 및 페이징 처리 포함)
     * @param searchDto
     * @param pageable
     * @return
     */
    @Override
    public Page<AnimalListResDto> findList(AnimalSearchDto searchDto, Pageable pageable) {
        // 조건에 맞는 유기동물 데이터 조회 (페이징 적용)
        List<AnimalListResDto> list = jpaQueryFactory.select(
                Projections.constructor(AnimalListResDto.class,
                        animal.postId,
                        animal.userId,
                        animal.thumbnailImage,
                        animal.title,
                        animal.content,
                        animal.viewCount,
                        animal.petCategory,
                        animal.petKind,
                        animal.age,
                        animal.vaccine,
                        animal.sexCode,
                        animal.neuterYn,
                        animal.address,
                        animal.fee,
                        animal.active,
                        animal.reservationStatus
                ))
                .from(animal)
                .where(builderCondition(searchDto), animal.active.eq(true))
                .orderBy(animal.createAt.desc())
                .offset(pageable.getOffset())       // 페이지 번호 기반 오프셋 적용
                .limit(pageable.getPageSize())      // 한 페이지 크기 제한
                .fetch();

        // 전체 데이터 개수 조회 (페이징을 위해 필요)
        Long count = jpaQueryFactory
                .select(animal.count())
                .from(animal)
                .where(builderCondition(searchDto), animal.active.eq(true))
                .fetchOne();

        // Page 객체로 변환하여 반환
        return new PageImpl<>(list, pageable, count == null ? 0L : count);
    }

    // 검색 조건(QueryDSL)을 구성하는 메서드
    private BooleanBuilder builderCondition(AnimalSearchDto searchDto) {
        BooleanBuilder builder = new BooleanBuilder();

        // 보호소 주소 검색 (시/도 단위부터 검색 가능)
        if (searchDto.getAddress() != null && !searchDto.getAddress().isBlank()) {
            builder.and(animal.address.startsWithIgnoreCase(searchDto.getAddress()));
        }

        // 축종명(개/고양이 등) 필터
        if (searchDto.getPetCategory() != null && !searchDto.getPetCategory().isBlank()) {
            builder.and(animal.petCategory.eq(searchDto.getPetCategory()));
        }

        // 성별(M,F,Q) 필터
        if (searchDto.getSex() != null) {
            builder.and(animal.sexCode.eq(searchDto.getSex()));
        }

        // 통합 검색어가 있는 경우
        if (searchDto.getSearchWord() != null && !searchDto.getSearchWord().isBlank()) {
            String keyword = searchDto.getSearchWord();
            BooleanBuilder searchBuilder = new BooleanBuilder();

            // 사용자가 입력한 검색어가 다음 필드들에 포함되는지 검사
            searchBuilder.or(animal.title.containsIgnoreCase(keyword));      // 제목
            searchBuilder.or(animal.petKind.containsIgnoreCase(keyword));    // 품종명
            searchBuilder.or(animal.age.containsIgnoreCase(keyword));        // 나이
            searchBuilder.or(animal.address.containsIgnoreCase(keyword));    // 지역
            // 책임비(fee) 검색 처리
            if ("무료".equalsIgnoreCase(keyword.trim())) {
                // "무료" 입력 시 → 책임비가 0인 경우 검색
                searchBuilder.or(animal.fee.eq(0));
            } else {
                try {
                    // 숫자 입력 시 → 해당 금액과 일치하는 책임비 검색
                    Integer feeValue = Integer.parseInt(keyword.trim());
                    searchBuilder.or(animal.fee.eq(feeValue));
                } catch (NumberFormatException e) {
                    // 숫자가 아니면 책임비 검색은 생략
                }
            }

            // 모든 조건을 하나의 BooleanBuilder에 and로 묶음
            builder.and(searchBuilder);
        }

        return builder;
    }
}
