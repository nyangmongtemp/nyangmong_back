package com.playdata.adminservice.admin.repository.custom.Impl;


import com.playdata.adminservice.admin.dto.board.BoardSearchDto;
import com.playdata.adminservice.admin.dto.board.res.BoardListResDto;
import com.playdata.adminservice.admin.entity.Category;
import com.playdata.adminservice.admin.repository.custom.BoardRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.List;

import static com.playdata.adminservice.admin.entity.QAnimal.animal;
import static com.playdata.adminservice.admin.entity.QBoard.board;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BoardListResDto> findByList(BoardSearchDto boardSearchDto, Category category, Pageable pageable) {

        // 게시글 목록 조회 (조건 + 페이징 적용)
        List<BoardListResDto> content = jpaQueryFactory.select(
                        Projections.constructor(BoardListResDto.class,
                                board.postId,
                                board.title,
                                board.content,
                                board.nickname,
                                board.thumbnailImage,
                                board.category,
                                board.viewCount,
                                board.createAt,
                                board.updateAt
                        ))
                .from(board)
                .where(createCondition(boardSearchDto)) // 동적 검색 조건
                // active true 인 애들을 찾겠다, 들어온 category가 board에 있는 카테고리와 같은 데이터를 찾겠다
                .where(board.active.eq(true).and(board.category.eq(category)))
                .offset(pageable.getOffset()) // 시작 위치 , 페이지 수
                .limit(pageable.getPageSize()) // 가져올 개수 , 설정 해 놓은 한페이지에 가져올 수
                .fetch();

        // 전체 데이터 개수 조회 (페이징을 위해 필요)
        Long count = jpaQueryFactory
                .select(board.count())
                .from(board)
                .where(createCondition(boardSearchDto), board.active.eq(true))
                .fetchOne();

        // Page 객체로 리턴
        return new PageImpl<>(content, pageable, count);
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
