package com.playdata.adminservice.admin.repository.custom.Impl;

import com.playdata.adminservice.admin.repository.custom.AdminLogRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminLogRepositoryImpl implements AdminLogRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

}
