package com.playdata.mainservice.main.repository;

import com.playdata.mainservice.main.entity.Category;
import com.playdata.mainservice.main.entity.ContentType;
import com.playdata.mainservice.main.entity.Like;
import com.playdata.mainservice.main.entity.QLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;


}
