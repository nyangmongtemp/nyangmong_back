package com.playdata.boardservice.board.repository.custom.Impl;

import static com.playdata.boardservice.board.entity.QInformationBoard.informationBoard;

import com.playdata.boardservice.board.dto.BoardSearchDto;
import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.entity.InformationBoard;
import com.playdata.boardservice.board.repository.custom.InformationBoardRepositoryCustom;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class InformationBoardRepositoryImpl implements InformationBoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<InformationBoard> findList(BoardSearchDto boardSearchDto, Category category, Pageable pageable) {
        // 게시글 목록 조회 (조건 + 페이징 적용)
        List<InformationBoard> content = jpaQueryFactory.selectFrom(informationBoard) // 여기있는 내용 다 보겠다
                .where(createCondition(boardSearchDto)) // 동적 검색 조건
                .where(informationBoard.active.eq(true)) // active true 인 애들을 찾겠다
                .offset(pageable.getOffset()) // 시작 위치 , 페이지 수
                .limit(pageable.getPageSize()) // 가져올 개수 , 설정 해 놓은 한페이지에 가져올 수
                .fetch();

        // 전체 게시글 수 조회 (count 쿼리에는 offset/limit 적용하지 않음)
        long total = jpaQueryFactory.select(informationBoard.count())
                .from(informationBoard)
                .where(createCondition(boardSearchDto))
                .fetchOne();

        // Page 객체로 리턴
        return new PageImpl<>(content, pageable, total);
    }

    // 정보 게시판 메인에 최신 게시물 조회
    public List<InformationBoard> findMainList() {
        return jpaQueryFactory.selectFrom(informationBoard)
                .fetch();
    }

    // 동적 조건 생성 메서드
    private Predicate createCondition(BoardSearchDto boardSearchDto) {
        BooleanBuilder builder = new BooleanBuilder();

        // 제목 검색 조건
        if (boardSearchDto.getTitle() != null && !boardSearchDto.getTitle().isBlank()) {
            builder.and(informationBoard.title.containsIgnoreCase(boardSearchDto.getTitle()));
        }

        // 닉네임 검색 조건
        if (boardSearchDto.getNickname() != null && !boardSearchDto.getNickname().isBlank()) {
            builder.and(informationBoard.nickname.containsIgnoreCase(boardSearchDto.getNickname()));
        }

        // 본문 전체 검색
        if (boardSearchDto.getContent() != null && !boardSearchDto.getContent().isBlank()) {
            builder.and(informationBoard.content.containsIgnoreCase(boardSearchDto.getContent()));
        }

        // 카테고리
        if (boardSearchDto.getCategory() != null) {
            builder.and(informationBoard.category.eq(boardSearchDto.getCategory().name()));
        }


        return builder;
    }

}
