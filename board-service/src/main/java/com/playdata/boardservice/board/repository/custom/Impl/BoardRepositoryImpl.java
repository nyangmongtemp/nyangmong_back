package com.playdata.boardservice.board.repository.custom.Impl;

import com.playdata.boardservice.board.dto.BoardSearchDto;
import com.playdata.boardservice.board.entity.Board;
import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.repository.custom.BoardRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.playdata.boardservice.board.entity.QBoard.board;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Board> findList(BoardSearchDto boardSearchDto, Category category, Pageable pageable) {

        // 게시글 목록 조회 (조건 + 페이징 적용)
        List<Board> content = jpaQueryFactory.selectFrom(board) // 여기있는 내용 다 보겠다
                .where(createCondition(boardSearchDto)) // 동적 검색 조건
                // active true 인 애들을 찾겠다, 들어온 category가 board에 있는 카테고리와 같은 데이터를 찾겠다
                .where(board.active.eq(true).and(board.category.eq(category)))
                .offset(pageable.getOffset()) // 시작 위치 , 페이지 수
                .limit(pageable.getPageSize()) // 가져올 개수 , 설정 해 놓은 한페이지에 가져올 수
                .fetch();

        // 전체 데이터 개수 조회 (페이징을 위해 필요)
        Long count = 0L;
        if (!CollectionUtils.isEmpty(content)) {
            count = jpaQueryFactory.select(board.count().coalesce(0L).as("cnt"))
                    .from(board)
                    .where(createCondition(boardSearchDto))
                    .fetchOne();
        }

        // Page 객체로 리턴
        return new PageImpl<>(content, pageable, count);
    }

    // 메인에 최신 게시물 조회 (자유, 후기, 질문)
    public List<Board> findMainList() {
        return jpaQueryFactory.selectFrom(board)
                // 지정한 카테고리의 값만 가져오겠다.
                .where(board.category.in(Category.FREE, Category.REVIEW, Category.QUESTION))
                .orderBy(board.createAt.desc())
                .limit(10L)
                .fetch();
    }

    // 게시판 메인에 인기 게시물 조회 (자유, 후기, 질문)
    public List<Board> findPopularList(int limit, int days) {
        return jpaQueryFactory
                .selectFrom(board) // 게시판 엔티티에서
                .where(board.category.in(Category.FREE, Category.REVIEW, Category.QUESTION))
                .where(board.active.eq(true) // 활성화된 게시글만
                        .and(board.createAt.after(LocalDateTime.now().minusDays(days)))) // 최근 N일 이내
                .orderBy(board.viewCount.desc()) // 조회수 내림차순 정렬
                .limit(limit) // 상위 N개만
                .fetch(); // 결과 가져오기
    }

    // 동적 조건 생성 메서드
    private BooleanBuilder createCondition(BoardSearchDto boardSearchDto) {
        BooleanBuilder builder = new BooleanBuilder();

        // 제목 검색 조건
        if (boardSearchDto.getTitle() != null && !boardSearchDto.getTitle().isBlank()) {
            builder.and(board.title.containsIgnoreCase(boardSearchDto.getTitle()));
        }

        // 닉네임 검색 조건
        if (boardSearchDto.getNickname() != null && !boardSearchDto.getNickname().isBlank()) {
            builder.and(board.nickname.containsIgnoreCase(boardSearchDto.getNickname()));
        }

        // 본문 전체 검색
        if (boardSearchDto.getContent() != null && !boardSearchDto.getContent().isBlank()) {
            builder.and(board.content.containsIgnoreCase(boardSearchDto.getContent()));
        }

        // 카테고리
        if (boardSearchDto.getCategory() != null) {
            builder.and(board.category.eq(boardSearchDto.getCategory()));
        }


        return builder;
    }
}
