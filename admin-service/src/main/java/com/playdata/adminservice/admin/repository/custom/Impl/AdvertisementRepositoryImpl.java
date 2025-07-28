package com.playdata.adminservice.admin.repository.custom.Impl;

import com.playdata.adminservice.admin.dto.req.AdSearchDto;
import com.playdata.adminservice.admin.entity.Advertisement;
import com.playdata.adminservice.admin.entity.QAdvertisement;
import com.playdata.adminservice.admin.repository.custom.AdvertisementRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * 광고 검색 및 커스텀 쿼리 구현체
 * AdvertisementRepositoryCustom 인터페이스 구현
 * QueryDSL 기반 동적 조건 검색과 Native SQL 기반 랜덤 조회 기능 제공
 */
@RequiredArgsConstructor
public class AdvertisementRepositoryImpl implements AdvertisementRepositoryCustom {

    // QueryDSL의 핵심 객체로, JPQL을 타입 안전하게 작성할 수 있게 도와줌
    private final JPAQueryFactory queryFactory;

    // NativeQuery 실행을 위한 EntityManager
    @PersistenceContext
    private EntityManager em;

    // Q타입 객체 (QueryDSL이 자동 생성)
    private final QAdvertisement ad = QAdvertisement.advertisement;

    /**
     * 광고 목록을 동적 조건 + 페이징으로 조회
     *
     * @param searchDto 검색 조건 (id, 제목, 활성 여부, 시작일/종료일 등)
     * @param pageable  페이징 정보 (페이지 번호, 크기 등)
     * @return 조건에 맞는 광고 목록 페이지
     */
    @Override
    public Page<Advertisement> getAdsList(AdSearchDto searchDto, Pageable pageable) {
        QAdvertisement ad = QAdvertisement.advertisement;

        // 1. 실제 콘텐츠 목록 조회
        List<Advertisement> content = queryFactory
                .select(ad)
                .from(ad)
                .where(

                        titleContains(searchDto.getTitle()),
                        isActive(searchDto.getActive()),
                        betweenStartDate(searchDto.getStartDate()),
                        betweenEndDate(searchDto.getEndDate())
                )
                .orderBy(ad.id.asc())               // id 기준 정렬
                .offset(pageable.getOffset())       // 페이지 시작 위치
                .limit(pageable.getPageSize())      // 한 페이지당 항목 수
                .fetch();

        // 2. 전체 데이터 개수 조회
        Long total = queryFactory
                .select(ad.count())
                .from(ad)
                .where(

                        titleContains(searchDto.getTitle()),
                        isActive(searchDto.getActive()),
                        betweenStartDate(searchDto.getStartDate()),
                        betweenEndDate(searchDto.getEndDate())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 제목에 특정 문자열이 포함되었는지 조건 (대소문자 무시)
     */
    private BooleanExpression titleContains(String title) {
        return StringUtils.hasText(title) ? QAdvertisement.advertisement.title.containsIgnoreCase(title) : null;
    }

    /**
     * 활성화 여부 조건
     */
    private BooleanExpression isActive(Boolean active) {
        return active != null ? QAdvertisement.advertisement.active.eq(active) : null;
    }

    /**
     * 시작일이 지정한 날짜 이후인 광고 조건
     */
    private BooleanExpression betweenStartDate(LocalDate date) {
        return date != null ? QAdvertisement.advertisement.startDate.goe(date) : null;
    }

    /**
     * 종료일이 지정한 날짜 이전인 광고 조건
     */
    private BooleanExpression betweenEndDate(LocalDate date) {
        return date != null ? QAdvertisement.advertisement.endDate.loe(date) : null;
    }



    /**
     * 승인되지 않았고 활성화된 광고를 무작위로 limit개 조회 (Native SQL 사용)
     *
     * @param limit 조회할 광고 수
     * @return 무작위로 선택된 광고 리스트
     */
    @Override
    public List<Advertisement> findByConfirmedFalseRandomLimit(int limit) {
        // MySQL의 RAND()를 사용하여 무작위 정렬 후 제한된 개수만 조회
        String nativeSql = "SELECT * FROM advertisements WHERE confirmed = false AND active = true ORDER BY RAND() LIMIT :limit";
        Query query = em.createNativeQuery(nativeSql, Advertisement.class);
        query.setParameter("limit", limit);
        return query.getResultList();
    }

    /**
     * 승인되고(active = true) 활성화된 광고를 모두 조회
     *
     * @return 승인 및 활성화된 광고 리스트
     */
    @Override
    public List<Advertisement> findByConfirmedTrueAndActiveTrue() {
        return queryFactory
                .selectFrom(ad)
                .where(ad.confirmed.isTrue(), ad.active.isTrue())
                .fetch();
    }
}