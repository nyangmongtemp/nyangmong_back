package com.playdata.mainservice.main.repository.impl;

import com.playdata.mainservice.main.entity.Category;
import com.playdata.mainservice.main.entity.ContentType;
import com.playdata.mainservice.main.entity.Like;
import com.playdata.mainservice.main.repository.custom.LikeRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.playdata.mainservice.main.entity.QLike.like;

@Repository
public class LikeRepositoryImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public LikeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Like> findByCategoryAndContentTypeAndContentIdAndUserId(Category category, ContentType contentType, Long contentId, Long userId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(like)
                .where(
                        like.category.eq(category),
                        like.contentType.eq(contentType),
                        like.contentId.eq(contentId),
                        like.userId.eq(userId)
                )
                .fetchOne());
    }

    @Override
    public Long countByContentTypeAndCategoryAndContentIdAndActiveIsTrue(ContentType contentType, Category category, Long contentId) {
        return queryFactory
                .select(like.count())
                .from(like)
                .where(
                        like.contentType.eq(contentType),
                        like.category.eq(category),
                        like.contentId.eq(contentId),
                        like.active.isTrue()
                )
                .fetchOne();
    }

    @Override
    public Long countByContentTypeAndContentIdAndActiveTrue(ContentType contentType, Long contentId) {
        return queryFactory
                .select(like.count())
                .from(like)
                .where(
                        like.contentType.eq(contentType),
                        like.contentId.eq(contentId),
                        like.active.isTrue()
                )
                .fetchOne();
    }

}
