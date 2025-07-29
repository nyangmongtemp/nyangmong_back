package com.playdata.mapservice.map.repository.custom;

import com.playdata.mapservice.map.entity.CultureDetail.*;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class
CultureRepositoryImpl implements CultureRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<String> getRegionDetail(String addressCode, String category) {
        EntityPathBase<?> targetEntity = getEntityPath(category);
        String sido = CultureAddressCode.from(addressCode).getDesc();

        if (targetEntity == null) {
            throw new IllegalArgumentException("지원하지 않는 category입니다.");
        }
        if (sido == null) {
            throw new IllegalArgumentException("지원하지 않는 지역값 입니다.");
        }

        // 동적으로 선택할 필드
        Path<String> selectField = getSelectFieldByInstance(targetEntity, sido);

        // 동적으로 sido 비교 조건
        BooleanExpression sidoCondition = getSidoConditionByInstance(targetEntity, sido);

        return jpaQueryFactory
                .selectDistinct(selectField)
                .from(targetEntity)
                .where(sidoCondition)
                .fetch();
    }

    //  엔티티 동적 선택 메소드
    private EntityPathBase<?> getEntityPath(String entityName) {
        return switch (entityName) {
            case "style" -> QPetStyle.petStyle;
            case "cafe" -> QPetCafe.petCafe;
            case "shop" -> QPetShop.petShop;
            case "museum" -> QMuseum.museum;
            case "art" -> QArt.art;
            case "literary" -> QLiteraryCenter.literaryCenter;
            case "drug" -> QDrugStore.drugStore;
            default -> null;
        };
    }

    //  컬럼 동적 선택 (legalDong or sigungu)
    @SuppressWarnings("unchecked")
    private Path<String> getSelectFieldByInstance(EntityPathBase<?> entity, String sido) {
        boolean isSejong = "세종특별자치시".equals(sido);

        if (entity instanceof QPetStyle q) return isSejong ? q.legalDong : q.sigungu;
        if (entity instanceof QPetCafe q) return isSejong ? q.legalDong : q.sigungu;
        if (entity instanceof QPetShop q) return isSejong ? q.legalDong : q.sigungu;
        if (entity instanceof QMuseum q) return isSejong ? q.legalDong : q.sigungu;
        if (entity instanceof QArt q) return isSejong ? q.legalDong : q.sigungu;
        if (entity instanceof QLiteraryCenter q) return isSejong ? q.legalDong : q.sigungu;
        if (entity instanceof QDrugStore q) return isSejong ? q.legalDong : q.sigungu;

        throw new IllegalArgumentException("지원하지 않는 entity 타입입니다.");
    }

    //  sido 비교 조건
    private BooleanExpression getSidoConditionByInstance(EntityPathBase<?> entity, String sido) {
        if (entity instanceof QPetStyle q) return q.sido.eq(sido);
        if (entity instanceof QPetCafe q) return q.sido.eq(sido);
        if (entity instanceof QPetShop q) return q.sido.eq(sido);
        if (entity instanceof QMuseum q) return q.sido.eq(sido);
        if (entity instanceof QArt q) return q.sido.eq(sido);
        if (entity instanceof QLiteraryCenter q) return q.sido.eq(sido);
        if (entity instanceof QDrugStore q) return q.sido.eq(sido);

        throw new IllegalArgumentException("지원하지 않는 entity 타입입니다.");
    }


}
