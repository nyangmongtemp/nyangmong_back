package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.board.AnimalSearchDto;
import com.playdata.adminservice.admin.dto.board.BoardSearchDto;
import com.playdata.adminservice.admin.dto.board.res.AnimalListResDto;
import com.playdata.adminservice.admin.dto.board.res.BoardListResDto;
import com.playdata.adminservice.admin.dto.board.res.BoardResDto;
import com.playdata.adminservice.admin.entity.Animal;
import com.playdata.adminservice.admin.entity.Board;
import com.playdata.adminservice.admin.entity.Category;
import com.playdata.adminservice.admin.repository.AnimalRepository;
import com.playdata.adminservice.admin.repository.BoardRepository;
import com.playdata.adminservice.common.auth.TokenAdminInfo;
import com.playdata.adminservice.common.dto.CommonResDto;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import com.playdata.adminservice.common.exception.CommonException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminBoardService {

    private final AnimalRepository animalRepository;
    private final BoardRepository boardRepository;

    /**
     *
     * @param boardSearchDto
     * @param category
     * @param pageable
     * @return
     */
    // 게시판 게시물 목록 조회
    public Page<BoardListResDto> findInformationBoardList(BoardSearchDto boardSearchDto,
                                                          Category category,
                                                          Pageable pageable) {

        return boardRepository.findByList(boardSearchDto, category, pageable);
    }

    /**
     * 분양 게시물 목록 조회 (검색 및 페이징 포함)
     *
     * @param searchDto 검색 필터 조건
     * @param pageable 페이징 조건 (페이지 번호, 사이즈, 정렬 등)
     * @return AnimalListResDto로 매핑된 Page 객체 반환
     */
    public Page<AnimalListResDto> findStrayAnimalList(AnimalSearchDto searchDto, Pageable pageable) {

        return animalRepository.findList(searchDto, pageable);
    }



    /**
     *
     * @param category
     * @param postId
     * @param email
     * @param request
     * @return
     */
    // 게시판 게시물 상세 조회
    public CommonResDto boardDetail(Category category, Long postId, String email, HttpServletRequest request) {

        // 게시물 조회 (null 방지)
        Board board = boardRepository.findByPostIdAndCategoryAndActiveTrue(postId, category);

        if (board == null) {
            throw new CommonException(ErrorCode.DATA_NOT_FOUND);
        }

        // 화면단으로 보낼 DTO로 변환
        BoardResDto resDto = board.fromEntity(board);

        return new CommonResDto(HttpStatus.OK, "소개 게시물 조회 성공", resDto);
    }

    /**
     * 분양 게시물 상세 조회 (조회수 중복 방지 및 증가 포함)
     *
     * @param postId 게시물 ID
     * @param email 로그인 사용자 이메일 (null 가능)
     * @param request 사용자 요청 정보 (IP, User-Agent 추출용)
     * @return 조회된 Animal Entity
     */
    public Animal findByAnimal(Long postId, String email, HttpServletRequest request) {
        // 게시물 존재 여부 확인 (예외 처리 포함)
        Animal animal = Optional.ofNullable(animalRepository.findByPostIdAndActiveTrue(postId))
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));

        return animal;
    }

    /**
     *
     * @param postId
     * @param category
     * @param adminInfo
     */
    // 삭제
    @Transactional
    public void deleteBoard(Long postId, Category category, TokenAdminInfo adminInfo) {

        // 카테고리가 ANIMAL 일때
        if (category == Category.ANIMAL) {
            // 게시글 존재 여부
            Animal animal = animalRepository.findByPostIdAndActiveTrue(postId);

            // 게시글이 없으면 에러
            if (animal == null) {
                throw new CommonException(ErrorCode.DATA_NOT_FOUND);
            }

            animal.boardDelete();
        } else { // 카테고리가 ANIMAL을 제외한 다른 카테고리 일 때
            Board board = boardRepository.findByPostIdAndCategoryAndActiveTrue(postId, category);

            if (board == null) {
                throw new CommonException(ErrorCode.DATA_NOT_FOUND);
            }

            board.boardDelete();
        }
    }


}
