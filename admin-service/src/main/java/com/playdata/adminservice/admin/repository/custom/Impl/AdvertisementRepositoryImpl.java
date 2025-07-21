package com.playdata.adminservice.admin.repository.custom.Impl;

import com.playdata.adminservice.admin.dto.req.AdSearchDto;
import com.playdata.adminservice.admin.entity.Advertisement;
import com.playdata.adminservice.admin.entity.QAdvertisement;
import com.playdata.adminservice.admin.repository.custom.AdvertisementRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * 광고 검색 관련 QueryDSL 구현체
 */
@RequiredArgsConstructor
public class AdvertisementRepositoryImpl implements AdvertisementRepositoryCustom {

    private final JPAQueryFactory queryFactory;





    /**
     * 광고 목록 검색 및 페이징 처리
     *
     * @param searchDto 검색 조건
     * @param pageable  페이징 조건
     * @return 페이징된 광고 목록 DTO
     */
    @Override
    public Page<Advertisement> searchAds(AdSearchDto searchDto, Pageable pageable) {
        QAdvertisement ad = QAdvertisement.advertisement;

        // 실제 광고 데이터 조회
        List<Advertisement> content = queryFactory
                .select(ad)
                .from(ad)
                .where(
                        idEq(searchDto.getId()),
                        titleContains(searchDto.getTitle()),     // 제목 포함 검색
                        isActive(searchDto.getActive()),         // 활성 여부 필터링
                        betweenStartDate(searchDto.getStartDate()), // 시작일 이후
                        betweenEndDate(searchDto.getEndDate())      // 종료일 이전
                )
                .orderBy(ad.orderNum.asc())    // 정렬: orderNum 오름차순
                .offset(pageable.getOffset()) // 시작 위치
                .limit(pageable.getPageSize()) // 페이지 크기
                .fetch();

        // 총 데이터 수 조회
        Long total = queryFactory
                .select(ad.count())
                .from(ad)
                .where(
                        idEq(searchDto.getId()),
                        titleContains(searchDto.getTitle()),
                        isActive(searchDto.getActive()),
                        betweenStartDate(searchDto.getStartDate()),
                        betweenEndDate(searchDto.getEndDate())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * 제목 검색 조건 (대소문자 무시, 포함 여부)
     */
    private BooleanExpression titleContains(String title) {
        return StringUtils.hasText(title) ? QAdvertisement.advertisement.title.containsIgnoreCase(title) : null;
    }

    /**
     * 활성 여부 조건
     */
    private BooleanExpression isActive(Boolean active) {
        return active != null ? QAdvertisement.advertisement.active.eq(active) : null;
    }

    /**
     * 시작일이 지정된 날짜 이후인 조건
     */
    private BooleanExpression betweenStartDate(LocalDate date) {
        return date != null ? QAdvertisement.advertisement.startDate.goe(date) : null;
    }

    /**
     * 종료일이 지정된 날짜 이전인 조건
     */
    private BooleanExpression betweenEndDate(LocalDate date) {

        return date != null ? QAdvertisement.advertisement.endDate.loe(date) : null;
    }

    /**
     * 해당 아이디로 검색하는 조건
     */

    private BooleanExpression idEq(Long id) {
        return id != null ? QAdvertisement.advertisement.id.eq(id) : null;
    }


    /**
     * 요청한 orderNum 리스트 중, 현재 수정 대상이 아닌 광고들 중에서
     * 동일한 orderNum을 가진 광고가 DB에 존재하는지 조회합니다.
     *
     * <중복 순서 체크 용도>
     * - 사용 예: 광고 순서를 변경할 때, 이미 다른 광고가 해당 순서를 사용 중인 경우를 방지
     *
     * @param orderNums    클라이언트가 요청한 광고 순서 번호 리스트
     * @param excludeIds   현재 수정 대상 광고들의 ID 리스트 (이 광고들은 중복 검사에서 제외)
     * @return             DB에 이미 존재하는 중복된 orderNum을 가진 광고 리스트
     */
    @Override
    public List<Advertisement> findConflictingOrderNums(List<Integer> orderNums, List<Long> excludeIds) {
        QAdvertisement ad = QAdvertisement.advertisement;

        return queryFactory
                .selectFrom(ad)
                .where(
                        ad.orderNum.in(orderNums),     // 요청한 순서 번호들 중
                        ad.id.notIn(excludeIds)        // 수정 대상이 아닌 광고들에서만 검색
                )
                .fetch();
    }

    @Override
    public List<Advertisement> findByOrderNumGreaterThan(Integer orderNum) {
        QAdvertisement ad = QAdvertisement.advertisement;

        return queryFactory
                .selectFrom(ad)
                // 주어진 orderNum보다 큰 순서 번호를 가진 광고들 중
                .where(ad.orderNum.gt(orderNum), ad.active.isTrue()) // 비활성화된 광고는 제외하고
                .orderBy(ad.orderNum.asc()) // 순서 번호 기준 오름차순 정렬
                .fetch(); // 결과 리스트 반환
    }

    // 특정 orderNum을 가진 활성 광고가 존재하는지 여부만 알려줌 (존재하면 true, 없으면 false)
    public boolean existsByOrderNum(Integer orderNum) {
        QAdvertisement ad = QAdvertisement.advertisement;

        Integer result = queryFactory
                .selectOne()
                .from(ad)
                .where(ad.orderNum.eq(orderNum), ad.active.isTrue())
                .fetchFirst(); // 있으면 1개만 가져오고 바로 끝냄

        return result != null;
    }
}