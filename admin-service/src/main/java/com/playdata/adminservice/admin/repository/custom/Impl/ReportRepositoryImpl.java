package com.playdata.adminservice.admin.repository.custom.Impl;

import static com.playdata.adminservice.admin.entity.QReport.report;

import com.playdata.adminservice.admin.dto.res.ReportListResDto;
import com.playdata.adminservice.admin.entity.QUser;
import com.playdata.adminservice.admin.repository.custom.ReportRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ReportListResDto> findReportList(Long userId) {
        QUser reporter = new QUser("reporter"); // 신고한 사람
        QUser accused = new QUser("accused"); // 신고당한 사람

        return jpaQueryFactory.select(
                        Projections.constructor(ReportListResDto.class,
                                report.reportId,
                                report.content,
                                report.category,
                                report.createAt,
                                report.accusedUserId,
                                reporter.userName,
                                reporter.email,
                                accused.userName,
                                accused.email

                        )
                )
                .from(report)
                .leftJoin(reporter).on(report.reportUserId.eq(reporter.userId))
                .leftJoin(accused).on(report.accusedUserId.eq(accused.userId))
                .where(report.treat.isFalse(), report.accusedUserId.eq(userId))
                .orderBy(report.createAt.desc())
                .fetch();
    }
}