package com.playdata.mainservice.main.service;

import com.playdata.mainservice.client.UserServiceClient;
import com.playdata.mainservice.common.auth.TokenUserInfo;
import com.playdata.mainservice.common.dto.CommonResDto;
import com.playdata.mainservice.common.enumeration.ErrorCode;
import com.playdata.mainservice.common.exception.CommonException;
import com.playdata.mainservice.main.dto.*;
import com.playdata.mainservice.main.entity.*;
import com.playdata.mainservice.main.repository.CommentRepository;
import com.playdata.mainservice.main.repository.LikeRepository;
import com.playdata.mainservice.main.repository.ReplyRepository;
import com.playdata.mainservice.main.repository.impl.LikeRepositoryImpl;
import com.querydsl.core.Tuple;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.playdata.mainservice.main.entity.QLike.like;

@Service
@Slf4j
@RequiredArgsConstructor
public class MainService {

    private final UserServiceClient userClient;

    private final LikeRepository likeRepository;

    private final CommentRepository commentRepository;

    private final ReplyRepository replyRepository;
    
    // queryDSL 사용하는 Repository 서비스
    private final LikeRepositoryImpl likeImpl;

    // 들어온 url의 값의 유효성 확인용
    private List<String> categoryList = List.of("free", "adopt", "introduction", "review", "question");
    private List<String> typeList = List.of("post", "comment", "reply");

    /**
     *
     * @param userId
     * @param reqDto  --> contentType, contentId, category
     * @return
     */
    // 좋아요를 통합적으로 생성하는 서비스 메소드
    public CommonResDto createLike(Long userId, MainLikeReqDto reqDto) {
        
        // 요청 Dto 값의 유효성을 확인
        if(!isValidCategory(reqDto.getCategory()) || !isValidContentType(reqDto.getContentType())) {
            throw new CommonException(ErrorCode.INVALID_PARAMETER, "옳지 않은 입력값입니다.");
        }
        
        // 유효한 값들이니 ENUM 값으로 변환  --> ENUM 값이 대문자라서 toUpperCase 적용
        Category cate = Category.valueOf(reqDto.getCategory().toUpperCase());
        ContentType ct = ContentType.valueOf(reqDto.getContentType().toUpperCase());
        
        // DB에 기존에 생성된 좋아요가 있는지 조회
        Optional<Like> foundLike
                = likeRepository.findByCategoryAndContentTypeAndContentIdAndUserId(cate, ct, reqDto.getContentId(), userId);

        // 기존에 좋아요를 눌렀던 이력이 있는 경우
        if (foundLike.isPresent()) {
            Like like = foundLike.get();
            // 기존에 좋아요의 active 값을 바꿈. --> 취소와 다시 좋아요를 한번에
            like.changeActive();
            likeRepository.save(like);
            return new CommonResDto(HttpStatus.OK, "좋아요 값이 변경됨",
                    // 새로 바뀐 좋아요 active 값을 리턴
                    like.isActive());
        }

        // 좋아요를 눌렀던 이력이 없는 경우
        Like like = new Like(userId, reqDto.getContentId(), ct, cate);
        likeRepository.save(like);
        return new CommonResDto(HttpStatus.CREATED, "좋아요가 생성됨",
                // 새로 생성됐으니 active는 무조건 true
                true);
    }

    /**
     *
     * @param reqDto  --> categoru, contentType, hidden, content
     * @param userId
     * @param nickname
     * @return
     */
    // 댓글 생성 메소드
    public CommonResDto createComment(MainComReqDto reqDto, Long userId, String nickname) {

        // 요청 Dto 값의 유효성을 확인
        if(!isValidCategory(reqDto.getCategory())) {
            throw new CommonException(ErrorCode.INVALID_PARAMETER, "옳지 않은 입력값입니다.");
        }

        // 유효한 값들이니 ENUM 값으로 변환  --> ENUM 값이 대문자라서 toUpperCase 적용
        Category cate = Category.valueOf(reqDto.getCategory().toUpperCase());

        // user-service로 부터 사용자의 프로필 이미지 수신
        ResponseEntity<String> responseEntity = userClient.getUserProfileImage(userId);

        if(responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR, "댓글 생성 중에 에러가 발생하였습니다.");
        }

        // 새로운 댓글 생성
        Comment newComment
                = new Comment(userId, cate, reqDto.getContentId()
                , reqDto.getContent(), reqDto.isHidden(), nickname, responseEntity.getBody());
        // DB에 저장
        commentRepository.save(newComment);

        return new CommonResDto(HttpStatus.CREATED, "댓글이 생성됨",
                // Dto 변환 해서 화면단으로 리턴
                getDetailResDto(newComment, 0L));
    }
    
    // 댓글 삭제 메소드
    /**
     * 댓글 삭제 메소드
     * @param commentId
     * @param userId
     * @return
     */
    public CommonResDto deleteComment(Long commentId, Long userId) {

        Comment foundComment = isValidComment(commentId, userId);
        foundComment.deleteComment();
        commentRepository.save(foundComment);

        return new CommonResDto(HttpStatus.OK, "댓글이 정상적으로 삭제되었습니다.", true);
    }

    /**
     *  댓글 수정 메소드
     * @param userId
     * @param reqDto  --> commentId, content
     * @return
     */
    // 댓글 수정 메소드
    public CommonResDto modifyComment(Long userId, ComModiReqDto reqDto) {

        Comment comment = isValidComment(reqDto.getCommentId(), userId);
        comment.mofifyComment(reqDto.getContent());
        Comment saved = commentRepository.save(comment);

        Long likeCount
                = likeRepository.countByContentTypeAndContentIdAndActiveTrue(ContentType.COMMENT, comment.getCommentId());

        return new CommonResDto(HttpStatus.OK,
                "댓글 내용이 수정되었습니다.", getDetailResDto(saved, likeCount));
    }

    /**
     *
     * @param userInfo
     * @param reqDto  --> commentId, content
     * @return
     */
    // 대댓글 생성 메소드
    public CommonResDto createReply(TokenUserInfo userInfo, ReplySaveReqDto reqDto) {
        
        // 대댓글을 작성할 댓글이 유효한 지 확인
        Comment foundComment = isPresentComment(reqDto);

        // user-service 에서 대댓글 작성자의 프로필 이미지를 전송 받음
        ResponseEntity<String> res = userClient.getUserProfileImage(userInfo.getUserId());
        if(res.getStatusCode() != HttpStatus.OK) {
            throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR, "대댓글 작성 중 오류가 발생하였습니다.");
        }
        
        // 대댓글 생성
        Reply createdReply = new Reply(userInfo.getUserId(), reqDto.getContent(), foundComment,
                userInfo.getNickname(), res.getBody());
        
        // 화면단에 전송할 dto 변환 및 DB 저장
        ReplyDetailResDto resDto = replyRepository.save(createdReply).fromEntity(0L);
        
        return new CommonResDto(HttpStatus.CREATED, "대댓글이 생성되었습니다.", resDto);
    }

    /**
     *
     * @param userId
     * @param replyId
     * @return
     */
    // 대댓글 삭제 로직
    public CommonResDto deleteReply(Long userId, Long replyId) {
        
        // 대댓글의 유효성 확인
        Reply validReply = isValidReply(userId, replyId);
        // 대댓글 비활성화 처리
        validReply.deleteReply();
        replyRepository.save(validReply);

        return new CommonResDto(HttpStatus.OK, "대댓글이 삭제되었습니다.", true);
    }

    /**
     *
     * @param userInfo
     * @param reqDto  --> commentId, content
     * @return
     */
    // 대댓글 수정 메소드
    public CommonResDto modifyReply(TokenUserInfo userInfo, ReplyModiReqDto reqDto) {

        // 대댓글 존재 여부 및 활성화 여부, 수정, 삭제 권한 여부 확인
        Reply validReply = isValidReply(userInfo.getUserId(), reqDto.getReplyId());

        // 대댓글에 매핑된 댓글의 존재 및 활성화 여부 확인
        ReplySaveReqDto saveReqDto
                = new ReplySaveReqDto(userInfo.getUserId(), validReply.getContent());
        isPresentComment(saveReqDto);

        // 대댓글의 댓글이 존재하고 수정 권한도 있는 경우
        // 수정 진행
        validReply.modifyReply(reqDto.getContent());
        replyRepository.save(validReply);

        // 기존 대댓글의 좋아요 개수 조회
        Long likeCount
                = likeRepository.countByContentTypeAndContentIdAndActiveTrue(ContentType.REPLY, reqDto.getReplyId());

        return new CommonResDto(HttpStatus.CREATED,
                "대댓글 수정이 완료되었습니다.", validReply.fromEntity(likeCount));
    }

    /**
     *
     * @param userId
     * @return
     */
    // 탈퇴한 회원의 모든 좋아요, 댓글, 대댓글의 active 값을 false 처리 하는 로직
    public CommonResDto deleteUserAll(Long userId) {
/*
        // 좋아요  --> 회원이 탈퇴를 진행해도, 좋아요 개수에는 영향이 없기로 서비스를 설계함.
        
        Optional<List<Like>> foundLike = likeRepository.findByUserId(userId);
        // 사용자가 만든 좋아요가 있는 경우에만 active false 작업 수행
        if(foundLike.isPresent()) {
            List<Like> likes = foundLike.get();
            likes.forEach(Like::deleteLike); // 상태만 먼저 수정
            likeRepository.saveAll(likes);   // 일괄 저장
        }
        */
        
        // 댓글
        Optional<List<Comment>> foundComment = commentRepository.findByUserId(userId);
        // 사용자가 작성한 댓글이 있는 경우에만 active false 작업 수행
        if(foundComment.isPresent()) {
            List<Comment> comments = foundComment.get();
            // 댓글 삭제 처리 시, 대댓글도 삭제 처리 진행
            comments.forEach(Comment::deleteComment);
            commentRepository.saveAll(comments);
        }
        
        // 대댓글  --> 필요 없을 수도 있지만, 미연의 사태를 방지하기 위해
        // 댓글 삭제 시, 매핑된 모든 대댓글을 active false하는 로직이 있지만
        // 혹시 모르는 경우를 대비해 대댓글도 모드 active false 처리 진행
        Optional<List<Reply>> foundReply = replyRepository.findByUserId(userId);
        // 사용자가 작성한 대댓글이 있는 경우에만 active false 작업 수행
        if(foundReply.isPresent()) {
            List<Reply> replies = foundReply.get();
            replies.forEach(Reply::deleteReply);
            replyRepository.saveAll(replies);
        }

        return new CommonResDto(HttpStatus.OK, "회원의 모든 댓글, 대댓글, 좋아요를 삭제하였습니다.", true);
    }

    /**
     * 
     * @param userId
     * @param encodedNickname  --> feign 요청을 위해 인코딩된 닉네임(한글)
     * @return
     */
    // 회원의 닉네임 변경 시, 저장된 모든 댓글, 대댓글의 닉네임 값 변경
    public CommonResDto changeUserNickname(Long userId, String encodedNickname) {
        
        // 전송된 nickname 디코딩
        String nickname = URLDecoder.decode(encodedNickname, StandardCharsets.UTF_8);
        // 사용자가 작성한 모든 댓글 조회
        Optional<List<Comment>> foundComment = commentRepository.findByUserId(userId);
        // 사용자가 작성한 댓글이 있는 경우에만 닉네임 변경 작업 수행
        if(foundComment.isPresent()) {
            List<Comment> comments = foundComment.get();
            // 활성화가 된 댓글들만 닉네임 변경시키기
            comments.stream().filter(Comment::isActive).forEach(comment -> {
                comment.modifyNickname(nickname);
            });
            commentRepository.saveAll(comments);
        }

        // 사용자가 작성한 모든 대댓글 조회
        Optional<List<Reply>> foundReply = replyRepository.findByUserId(userId);
        // 사용자가 작성한 대댓글이 있는 경우에만, 닉네임 변경 작업 수행
        if(foundReply.isPresent()) {
            List<Reply> replies = foundReply.get();
            // 활성화된 대댓글들만 닉네임 변경
            replies.stream().filter(Reply::isActive).forEach(reply -> {
                reply.modifyNickname(nickname);
            });
            replyRepository.saveAll(replies);
        }

        return new CommonResDto(HttpStatus.OK, "사용자의 모든 댓글, 대댓글의 닉네임이 변경되었습니다.", true);
    }

    /**
     * 
     * @param userId
     * @param profileImage
     * @return
     */
    // 회원의 프로필 이미지가 변경되었을 때, 해당 사용자가 작성한 댓글, 대댓글의 profileImage 값을 변경시키는 로직
    public CommonResDto changeUserProfile(Long userId, String profileImage) {

        Optional<List<Comment>> foundComment = commentRepository.findByUserId(userId);
        // 사용자가 작성한 활성화된 댓글이 있는 경우에만 변경 수행
        if(foundComment.isPresent()) {
            List<Comment> comments = foundComment.get();
            comments.stream().filter(Comment::isActive).forEach(comment -> {
                comment.modifyProfileImage(profileImage);
            });
            commentRepository.saveAll(comments);
        }
        // 사용자가 작성한 활성화된 대댓글이 있는 경우에만 변경 수행
        Optional<List<Reply>> foundReply = replyRepository.findByUserId(userId);
        if(foundReply.isPresent()) {
            List<Reply> replies = foundReply.get();
            replies.stream().filter(Reply::isActive).forEach(reply -> {
                reply.modifyProfileImage(profileImage);
            });
            replyRepository.saveAll(replies);
        }
        return new CommonResDto(HttpStatus.OK, "사용자의 모든 댓글, 대댓글의 프로필 이미지가 변경되었습니다.", true);
    }

    /**
     * 
     * @param contentList  --> <category, contentId> 를 리스트로 받음
     * @return
     */
    // 게시물 목록 조회 시 사용할 댓글, 좋아요 개수 조회
    public CommonResDto getLikeCommentCount(List<LikeComCountReqDto> contentList) {

        List<LikeComCountResDto> result = contentList.stream()
                .map((req) -> {
                    // 입력된 contentType과 category의 유효성 확인
                    isValidCategory(req.getCategory());

                    // 입력값 ENUM화
                    Category category = Category.valueOf(req.getCategory().toUpperCase());

                    // 해당 게시물 혹은 댓글, 대댓글 중에서 활성화된 좋아요의 개수만 카운팅
                    Long count = likeRepository.countByContentTypeAndCategoryAndContentIdAndActiveIsTrue(
                            ContentType.POST, category, req.getContentId());
                    
                    // 검색 대상 게시물들의 모든 댓글의 id를 리턴 --> 대댓글 검색용
                    List<Long> foundComment
                            = commentRepository.findActiveCommentIdsByCategoryAndContentId(category, req.getContentId());
                    
                    // 조회된 댓글의 개수 합산
                    long totalCount = foundComment.size();
                    
                    // 조회된 댓글들의 모든 대댓글 개수 리턴
                    Long replyCount = replyRepository.countAllActiveRepliesByCommentIds(foundComment);
                    
                    // 댓글 개수에 대댓글 합산
                    totalCount += replyCount;

                    // dto로 리턴
                    return getLikeComCountResDto(req, count, totalCount);
                })
                .collect(Collectors.toList());
        return new CommonResDto(HttpStatus.OK, "모든 댓글수, 좋아요 수 구함.", result);
    }

    /**
     *
     * @param req  --> category, contentId
     * @return
     */
    // 게시물 상세 조회 시, 좋아요, 댓글 개수를 리턴해주는 로직
    public CommonResDto getDetail(LikeComCountReqDto req) {

        // 입력된 contentType과 category의 유효성 확인
        isValidCategory(req.getCategory());

        // 입력값 ENUM화
        Category category = Category.valueOf(req.getCategory().toUpperCase());

        // 해당 게시물
        Long likeCount = likeRepository.countByContentTypeAndCategoryAndContentIdAndActiveIsTrue(
                ContentType.POST, category, req.getContentId());

        // 검색 대상 게시물들의 모든 댓글의 id를 리턴 --> 대댓글 검색용
        List<Comment> foundComment
                = commentRepository.findActiveByCategoryAndContentId(category, req.getContentId());

        // 조회된 댓글의 개수 합산
        long totalCount = foundComment.size();

        // 조회된 댓글들의 모든 대댓글 개수 리턴
        Long replyCount = replyRepository.countAllActiveRepliesByCommentIds(
                foundComment.stream().map(Comment::getCommentId).collect(Collectors.toList()));

        // 댓글 개수에 대댓글 합산
        totalCount += replyCount;
        
        // 화면단 전송용 dto로 변환
        LikeComCountResDto result = getLikeComCountResDto(req, likeCount, totalCount);

        return new CommonResDto(HttpStatus.OK, "해당 게시물의 좋아요, 댓글 개수 리턴", result);
    }

    /**
     *
     * @param reqDto  --> category, contentId
     * @param pageable
     * @return
     */
    // 게시물 상세 조회 시, 댓글의 정보들을 화면단으로 리턴해주는 메소드
    public CommonResDto getCommentDetail(LikeComCountReqDto reqDto, Pageable pageable) {
        
        // 입력된 category의 유효성 확인
        isValidCategory(reqDto.getCategory());
        Category category = Category.valueOf(reqDto.getCategory().toUpperCase());
        
        // 해당 게시물의 활성화된 모든 댓글을 page로 조회
        Page<Comment> foundComment
                // 게시물의 category로 조회
                = commentRepository.findActiveByCategoryAndContentId(category, reqDto.getContentId(), pageable);
        
        // 해당 댓글들의 모든 좋아요 개수를 계산하는 로직
        List<CommentDetailResDto> commentList = foundComment.stream().map(comment -> {
            // 해당 댓글의 활성화된 모든 좋아요 개수를 계산하는 로직
            Long likeCount = likeRepository.countByContentTypeAndCategoryAndContentIdAndActiveIsTrue(ContentType.COMMENT,
                    category, comment.getContentId());
            
            // 댓글의 정보 + 좋아요 개수 형태로 dto 변환
            // 댓글에 매핑된 대댓글의 여부도 포함
            return getDetailResDto(comment, likeCount);
        }).collect(Collectors.toList());

        return new CommonResDto(HttpStatus.OK, "해당 게시물의 모든 댓글 정보 조회", commentList);

    }

    /**
     *
     * @param userId
     * @param pageable
     * @return
     */
    // 마이페이지에서 사용자의 댓글 조회 --> 활성화된 모든 댓글을 페이징 처리해서 조회
    public CommonResDto getMyComment(Long userId, Pageable pageable) {

        // 해당 userId로 작성된 모든 활성화 댓글을 페이징 조건에 맞게 조회
        Page<Comment> foundUserComment = commentRepository.findActiveByUserId(userId, pageable);

        // 조회된 댓글의 좋아요 개수를 계산하는 로직
        List<CommentDetailResDto> myCommentList = foundUserComment.stream().map(comment -> {
            // 해당 댓글의 활성화된 모든 좋아요 개수를 계산하는 로직
            Long likeCount
                    = likeRepository.countByContentTypeAndContentIdAndActiveTrue(ContentType.COMMENT ,comment.getCommentId());

            // 댓글의 정보 + 좋아요개수 + 대댓글 존재 여부
            return getDetailResDto(comment, likeCount);
        }).collect(Collectors.toList());

        return new CommonResDto(HttpStatus.OK, "사용자의 모든 댓글 정보 조회", myCommentList);
    }

    /**
     *
     * @param userId
     * @param pageable
     * @return
     */
    // 마이페이지에서 사용자의 대댓글 조회 --> 활성화된 모든 대댓글을 페이징 처리해서 조회
    public CommonResDto getMyReply(Long userId, Pageable pageable) {

        // 사용자가 작성한 활성화된 모든 대댓글을 페이징 조건에 맞게 조회
        Page<Reply> foundUserReply = replyRepository.findActiveByUserId(userId, pageable);

        // 해당 대댓글의 좋아요 개수를 계산하는 로직
        List<ReplyDetailResDto> myReplyList = foundUserReply.stream().map(reply -> {
            // 대댓글들의 좋아요 개수를 계산하는 로직
            Long likeCount =
                    likeRepository.countByContentTypeAndContentIdAndActiveTrue(ContentType.REPLY, reply.getReplyId());

            // 대댓글의 정보 + 좋아요 개수
            return reply.fromEntity(likeCount);
        }).collect(Collectors.toList());

        return new CommonResDto(HttpStatus.OK, "사용자의 모든 대댓글 정보 조회", myReplyList);
    }

    /**
     *
     * @param userId  --> 작성자 userId
     * @param reqDto  --> commentId, 열람을 요청한 사용자 userId
     * @return
     */
    // 비공개 댓글 열람 권한을  확인하는 메소드
    public boolean canSeeHideComment(Long userId, SeeHideComReqDto reqDto) {

        // 열람 요청자와 게시물 작성자가 동일한 경우
        if(userId == reqDto.getUserId()) {
            return true;
        }
        Optional<Comment> foundComment = commentRepository.findById(reqDto.getCommentId());
        // 열람할 비공개 댓글이 존재하지 않거나, 삭제되었거나, 비공개 댓글이 아닌 경우
        if(!foundComment.isPresent() || !foundComment.get().isHidden()
                || !foundComment.get().isActive()) {
            throw new EntityNotFoundException("열람할 비공개 댓글이 없습니다.");
        }
        // 열람 요청자가 댓글 작성자가 아닌 경우
        if(foundComment.get().getUserId() != userId) {
            throw new IllegalArgumentException("비공개 댓글을 열람할 권한이 없습니다.");
        }
        return true;
    }
    
    // 메인 화면에 드러날 소개 게시판의 인기 게시물을 찾아서, 좋아요, 댓글 개수를 같이 리턴해주는 로직
    public CommonResDto getMainIntroduction() {
        
        // 소개 게시물 중 한 달동안 생성된 좋아요 개수가 가장 많은 3개의 게시물을 조회
        List<Tuple> postList = likeImpl.getPostIdMainIntroductionPost();
        List<LikeComCountResDto> resDtoList = postList.stream().map(tuple -> {
            // 해당 게시물의 댓글 개수 조회
            List<Long> findComment = commentRepository.
                    findActiveCommentIdsByCategoryAndContentId(Category.INTRODUCTION, tuple.get(like.contentId));
            // 댓글 개수
            Long commentCount = (long) findComment.size();
            // 댓글에 매핑된 대댓글 개수
            Long replyCount = replyRepository.countAllActiveRepliesByCommentIds(findComment);
            // 총 댓글 개수 = 댓글 + 대댓글
            commentCount += replyCount;
            // contentId, Category, 좋아요 개수, 총 댓글 개수
            return new LikeComCountResDto(tuple.get(like.contentId), "Introduction", tuple.get(like.count()), commentCount);
        }).collect(Collectors.toList());

        return new CommonResDto(HttpStatus.OK, "인기 소개 게시물 정보 조회", resDtoList);
    }

    public CommonResDto getUserLiked(Long userId, MainLikeReqDto reqDto) {

        if(!isValidCategory(reqDto.getCategory()) || !isValidContentType(reqDto.getContentType())) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        Optional<Like> liked = likeImpl.findUserLiked(userId, reqDto);
        return new CommonResDto(HttpStatus.OK, "사용자의 좋아요 찾음", liked.isPresent());
    }

    /////////////////// 공통 사용 메소드들입니다.

    // 들어온 요청의 url값의 유효성을 확인하는 메소드
    // 컨텐츠타입의 유효성 확인
    private boolean isValidContentType(String contentType) {
        return typeList.contains(contentType);
    }

    // 카테고리의 유효성 확인
    private boolean isValidCategory(String category) {
        return categoryList.contains(category);
    }

    // 댓글이 존재하고 삭제되지 않았는 지 판별해서 리턴해주는 메소드
    private Comment isValidComment(Long commentId, Long userId) {
        Optional<Comment> foundComment = commentRepository.findById(commentId);
        // 삭제하려는 댓글이 존재하지 않는 경우
        if(!foundComment.isPresent()) {
            throw new CommonException(ErrorCode.NOT_FOUND, "삭제하려는 댓글이 존재하지 않습니다.");
        }
        // 삭제 요청을 보낸 사용자가 댓글의 작성자가 아닌 경우
        if(!foundComment.get().getUserId().equals(userId)) {
            throw new CommonException(ErrorCode.NO_DELETE_PERMISSION);
        }
        return foundComment.get();
    }

    // 대댓글을 작성할 댓글이 유효한지 판별해주는 메소드
    private Comment isPresentComment(ReplySaveReqDto reqDto) {
        Optional<Comment> foundComment = commentRepository.findById(reqDto.getCommentId());
        // 대댓글을 작성하려는 댓글이 존재하지 않거나 삭제된 경우
        if(!foundComment.isPresent() || !foundComment.get().isActive()) {
            throw new CommonException(ErrorCode.NOT_FOUND, "삭제하려는 댓글이 존재하지 않습니다.");
        }
        return foundComment.get();
    }

    // 대댓글을 수정 및 삭제할 권한이 있는지 판별해주는 메소드
    private Reply isValidReply(Long userId, Long replyId) {
        Optional<Reply> foundReply = replyRepository.findById(replyId);
        // 삭제할 대댓글이 존재하지 않는 경우
        if(!foundReply.isPresent() || !foundReply.get().isActive()) {
            throw new CommonException(ErrorCode.NOT_FOUND, "삭제하려는 댓글이 존재하지 않습니다.");
        }
        // 삭제를 요청한 사용자가 대댓글 작성자가 아닌 경우
        if(!foundReply.get().getUserId().equals(userId)) {
            throw new CommonException(ErrorCode.NO_UPDATE_PERMISSION);
        }
        return foundReply.get();
    }

    // 게시물 상세 조회 시, 댓글 개수와 좋아요 수를 담은 dto 변환 메소드
    private static LikeComCountResDto getLikeComCountResDto(LikeComCountReqDto req, Long count, long totalCount) {
        return LikeComCountResDto.builder()
                .contentId(req.getContentId())
                .category(req.getCategory())
                .likeCount(count)
                .commentCount(totalCount)
                .build();
    }

    // 게시물 상세 조회 시, 댓글의 정보들 + 좋아요 수 + 대댓글 여부를 담은 dto 변환 메소드

    private static CommentDetailResDto getDetailResDto(Comment comment, Long likeCount) {
        return CommentDetailResDto.builder()
                .contentId(comment.getCommentId())
                .category(String.valueOf(comment.getCategory()))
                .content(comment.getContent())
                // 대댓글 존재 여부
                .isReply(!comment.getReplyList().isEmpty())
                .createAt(comment.getCreateAt())
                // 좋아요 개수
                .likeCount(likeCount)
                .profileImage(comment.getProfileImage())
                .nickname(comment.getNickname())
                .userId(comment.getUserId())
                // 비공개 여부
                .hidden(comment.isHidden())
                .commentId(comment.getCommentId())
                .build();
    }
}
