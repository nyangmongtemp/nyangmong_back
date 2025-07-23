package com.playdata.adminservice.admin.repository.custom.Impl;

import com.playdata.adminservice.admin.repository.custom.TermsRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TermsRepositoryImpl implements TermsRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


}
