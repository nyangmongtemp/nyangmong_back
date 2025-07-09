package com.playdata.mainservice.main.repository.impl;

import com.playdata.mainservice.main.dto.req.MainLikeReqDto;
import com.playdata.mainservice.main.entity.Category;
import com.playdata.mainservice.main.entity.Like;
import com.playdata.mainservice.main.repository.custom.LikeRepositoryCustom;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.playdata.mainservice.main.entity.QLike.like;

@Repository
public class LikeRepositoryImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public LikeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Optional<Like> findUserLiked(Long userId, MainLikeReqDto reqDto) {
        return Optional.ofNullable(queryFactory
                .selectFrom(like)
                .where(
                        like.active.isTrue(),
                        like.category.eq(Category.valueOf(reqDto.getCategory())),
                        like.contentId.eq(reqDto.getContentId()),
                        like.userId.eq(userId)
                )
        .fetchOne());
    }

    @Override
    public List<Tuple> getPostIdMainIntroductionPost() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);

        return queryFactory.select(like.contentId, like.count())
                .from(like)
                .where(
                        like.category.eq(Category.INTRODUCTION),
                        like.active.isTrue(),
                        like.createAt.after(oneMonthAgo)
                )
                .groupBy(like.contentId)
                .orderBy(like.count().desc())
                .limit(3)
                .fetch();
    }

}
