package com.playdata.mainservice.main.repository.custom;

import com.playdata.mainservice.main.dto.MainLikeReqDto;
import com.playdata.mainservice.main.entity.Like;
import com.querydsl.core.Tuple;

import java.util.List;
import java.util.Optional;

public interface LikeRepositoryCustom {

    // 로그인한 사용자가 특정 게시물에 좋아요롤 눌렀는지 확인하는 메소드
    Optional<Like> findUserLiked(Long userId, MainLikeReqDto reqDto);


    // 메인화면에서 한달간 좋아요 개수가 가장 많은 소개 게시물 3개의 contentId를 리턴해주는 메소드
    List<Tuple> getPostIdMainIntroductionPost();

}
