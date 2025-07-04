package com.playdata.boardservice.board.repository.custom.Impl;

import com.playdata.boardservice.board.dto.BoardSearchDto;
import com.playdata.boardservice.board.entity.IntroductionBoard;
import com.playdata.boardservice.board.repository.custom.IntroductionBoardRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.playdata.boardservice.board.entity.QIntroductionBoard.introductionBoard;


@RequiredArgsConstructor
public class IntroductionBoardRepositoryImpl implements  IntroductionBoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<IntroductionBoard> findList(BoardSearchDto boardSearchDto, Pageable pageable) {
        // 게시글 목록 조회
        List<IntroductionBoard> content = jpaQueryFactory.selectFrom(introductionBoard)
                .where(createCondition(boardSearchDto)) // 검색 조건
                .offset(pageable.getOffset()) // 시작 위치
                .limit(pageable.getPageSize()) // 페이지당 개수
                .fetch();

        // 전체 개수 조회 (페이징 계산용)
        long total = jpaQueryFactory.select(introductionBoard.count())
                .from(introductionBoard)
                .where(createCondition(boardSearchDto))
                .fetchOne();

        // Page 형태로 리턴
        return new PageImpl<>(content, pageable, total);
    }

    // 검색 조건 동적으로 생성
    private Predicate createCondition(BoardSearchDto boardSearchDto) {
        BooleanBuilder builder = new BooleanBuilder();

        // 제목 포함 검색
        if (boardSearchDto.getTitle() != null && !boardSearchDto.getTitle().isBlank()) {
            builder.and(introductionBoard.title.containsIgnoreCase(boardSearchDto.getTitle()));
        }

        // 작성자 닉네임 검색
        if (boardSearchDto.getNickname() != null && !boardSearchDto.getNickname().isBlank()) {
            builder.and(introductionBoard.nickname.containsIgnoreCase(boardSearchDto.getNickname()));
        }

        // 본문 내용 전체 검색
        if (boardSearchDto.getContent() != null && !boardSearchDto.getContent().isBlank()) {
            builder.and(introductionBoard.content.containsIgnoreCase(boardSearchDto.getContent()));
        }

        return builder;
    }

}
