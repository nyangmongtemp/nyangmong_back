package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.banner.req.BannerModiReqDto;
import com.playdata.adminservice.admin.dto.banner.req.BannerSaveReqDto;
import com.playdata.adminservice.admin.dto.banner.req.OrderModiReqDto;
import com.playdata.adminservice.admin.dto.banner.res.BannerListResDto;
import com.playdata.adminservice.admin.dto.banner.res.BannerSaveResDto;
import com.playdata.adminservice.admin.entity.Banner;
import com.playdata.adminservice.admin.entity.BannerCount;
import com.playdata.adminservice.admin.repository.BannerCountRepository;
import com.playdata.adminservice.admin.repository.BannerRepository;
import com.playdata.adminservice.common.configs.AwsS3Config;
import com.playdata.adminservice.common.dto.CommonResDto;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import com.playdata.adminservice.common.exception.CommonException;
import com.playdata.adminservice.common.util.ImageValidation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BannerService {

    private final BannerRepository bannerRepository;
    private final BannerCountRepository bannerCountRepository;

    // 기본 배너 이미지 1
    @Value("${imagePath.banner.default1}")
    private String defaultBannerImage1;
    // 기본 배너 이미지 2
    @Value("${imagePath.banner.default2}")
    private String defaultBannerImage2;

    private final AwsS3Config s3Config;

    // 초기 배너 노출 개수 -> 임의로 정한 값임. table이 처음 생성될 때만 이용됨.
    private final int bannerCount = 4;

    // 테이블이 초기로 생성이 되는 것이라면, 자동적으로 데이터를 넣어주는 로직
    public void init() {
        // 데이터의 개수가 없는 경우에만 작동
        if (bannerCountRepository.count() == 0) {
            bannerCountRepository.save(
                    BannerCount.builder()
                            // 초기값은 4으로 지정
                            .count(bannerCount)
                            .build()
            );
        }
        // 기본 배너 이미지 저장 로직
        if (bannerRepository.count() == 0) {
            Banner default1
                    = new Banner("기본 배너 1", -1L, defaultBannerImage1, 1);
            Banner default2
                    = new Banner("기본 배너 2", -1L, defaultBannerImage2, 2);
            bannerRepository.save(default1);
            bannerRepository.save(default2);
        }
    }

    @Transactional
    public CommonResDto createBanner(Long adminId, @Valid BannerSaveReqDto reqDto, MultipartFile thumbnailImage) {
    
        // 이미지 유효성 검증 및 지정된 경로에 이미지 저장
        String imageSaveUrl = setThumbnailImage(thumbnailImage);
        // 이미지가 없거나, 문제가 발생한 경우
        if(imageSaveUrl == null) {
            // 에러 발생
            throw new CommonException(ErrorCode.FILE_SERVER_ERROR, "이미지 저장 중에 오류 발생 혹은 이미지가 없음.");
        }

        reqDto.setThumbnailImage(imageSaveUrl);
        Banner banner = new Banner(adminId, reqDto, null);
        // 만일 order를 발급 받을 수 있다면
        if(isValidBanners()) {
            // order의 최댓값 + 1  해줌.
            int order = bannerRepository.getMaxOrder() + 1;
            banner.setOrder(order);
        }

        bannerRepository.save(banner);

        return new CommonResDto(HttpStatus.CREATED, "배너 생성됨", banner.toDetailDto());
    }

    @Transactional
    public CommonResDto updateBanner(Long adminId, @Valid BannerModiReqDto reqDto, MultipartFile thumbnailImage) {

        // id를 통한 배너 찾기 및, 유효성 검증
        Banner foundBanner = findBannerById(reqDto.getBannerId());
        // 이미지를 수정하는 경우
        if(thumbnailImage != null) {
            // 이미지 유효성 및 저장
            String thumbnailUrl = setThumbnailImage(thumbnailImage);
            foundBanner.setThumbnailImage(thumbnailUrl);
        }
        // 제목을 수정하는 경우
        if(reqDto.getTitle() != null) {
            foundBanner.setTitle(reqDto.getTitle());
        }
        // 최종 수정한 관리자 id 갱신
        foundBanner.setAdminId(adminId);

        return new CommonResDto(HttpStatus.OK, "배너 내용 수정됨", foundBanner.toDetailDto());
    }

    @Transactional
    public CommonResDto deleteBanner(Long adminId, Long bannerId) {

        // id를 통한 배너 찾기 및, 유효성 검증
        Banner foundBanner = findBannerById(bannerId);
        // 기본 배너인 경우, 삭제 불가
        if(foundBanner.isBasic()) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "기본 배너입니다.");
        }
        // order 삭제
        foundBanner.setOrder(null);
        // 삭제한 관리자 id 남기기
        foundBanner.setAdminId(adminId);
        // 비활성화 처리
        foundBanner.deleteBanner();

        return new CommonResDto(HttpStatus.OK, "배너 삭제됨", true);
    }

    @Transactional
    public CommonResDto updateOrder(Long adminId, @Valid List<OrderModiReqDto> reqDtoList) {

        long bannerLimitCount = bannerCountRepository.findById(1L).get().getCount();
        // 수정 데이터의 개수가 배너 노출 최대 개수보다 많은 경우
        if (reqDtoList.size() > bannerLimitCount) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "노출 최대 개수 초과");
        }
        // 수정하려는 데이터가 노출되고 있는 배너의 개수와 일치하지 않는 경우
        if(reqDtoList.size() != bannerRepository.countExposedBanners()) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "순서 수정 요청 배너 개수 이상");
        }
        // 요청 온 데이터의 유효성 검증
        validateUniqueBannerOrders(reqDtoList);
        
        reqDtoList.stream().forEach(reqDto -> {
            // 유효한 배너 찾기
            Banner foundBanner = findBannerById(reqDto.getBannerId());
            // 순서가 있는 지 확인
            if(foundBanner.getOrderNum() == null) {
                // 없으면 순서를 수정할 수 없음
                throw new CommonException(ErrorCode.BAD_REQUEST, "순서가 없는 배너입니다.");
            }
            // 순서 변경
            foundBanner.setOrder(reqDto.getOrder());
            // 이건 사실 필요 없을지도
            foundBanner.setAdminId(adminId);
        });
        
        // 순서 변경에 성공했다면 ,true를 리턴
        return new CommonResDto(HttpStatus.OK, "배너의 순서 수정됨", true);
    }

    @Transactional
    public CommonResDto enrollOrder(Long bannerId) {
        // 요청온 배너의 유효성 검증
        Banner foundBanner = findBannerById(bannerId);
        // 이미 순서가 있는 배너이거나, 기본 배너인 경우
        if(foundBanner.getOrderNum() != null || foundBanner.isBasic()) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "배너가 이미 노출되어있거나, 기본 배너입니다.");
        }
        // 현재 노출되어 있는 배너 개수 조회
        Integer exposedCount = bannerRepository.countExposedBanners();
        // 현재 노출시킬 수 있는 배너 개수 조회
        long limit = bannerCountRepository.findById(1L).get().getCount();
        // 요청온 배너를 노출시킬 수 없는 경우
        if(exposedCount >= limit) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "배너를 노출시킬 수 없습니다.");
        }
        // 현재 Order값 중 최댓값 + 1
        int newOrder = bannerRepository.getMaxOrder() + 1;
        // 순서값은 제일 마지막 순서로 입력
        foundBanner.setOrder(newOrder);

        return new CommonResDto(HttpStatus.OK, "노출할 배너로 등록됨", true);
    }

    @Transactional
    public CommonResDto cancelOrder(Long bannerId) {

        Banner foundBanner = findBannerById(bannerId);
        // 기본 배너 혹은 이미 노출되어 있지 않은 배너인 경우
        if(foundBanner.isBasic() || foundBanner.getOrderNum() == null) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "배너가 이미 노출되어있지 않거나, 기본 배너입니다.");
        }
        // 노출 해제
        foundBanner.setOrder(null);

        return new CommonResDto(HttpStatus.OK, "배너 노출 해제됨", true);
    }

    public CommonResDto getExposedList() {
        
        // 노출할 배너 조회
        List<BannerListResDto> resDto = bannerRepository.getExposedBanners().stream().map(Banner::toListDto).toList();

        return new CommonResDto(HttpStatus.OK, "노출시킬 배너 목록 조회", resDto);
    }

    public CommonResDto getUnorderedList(String keyword, int page) {
        
        // 조회용 pageable 객체 조립
        Pageable pageable 
                = PageRequest.of(page, 5, Sort.by(Sort.Order.desc("createAt")));
        
        // 찾아온 Banner들 -> dto 변환
        Page<BannerListResDto> result 
                = bannerRepository.getExposedBannersByKeyword(keyword, pageable).map(Banner::toListDto);

        return new CommonResDto(HttpStatus.OK, "배너들 페이징 조회해옴", result);
    }

    public CommonResDto findDetail(Long bannerId) {

        // 유효성 검증 및 객체 조회
        BannerSaveResDto resDto = findBannerById(bannerId).toDetailDto();

        return new CommonResDto(HttpStatus.OK, "해당 배너 상세 정보 조회", resDto);
    }

    public long getLimit() {

        long count = bannerCountRepository.findById(1L).get().getCount();

        return count;
    }
    
    @Transactional
    public CommonResDto updateLimit(Integer count) {

        // 혹시 bannerCount가 단일 행이 아니라면 나머지 전부 삭제
        validateBannerCount();
        BannerCount found = bannerCountRepository.findById(1L).get();
        Integer exLimit = found.getCount();
        // 현재 노출되고 있는 배너의 개수
        Integer nowExposedBannerCount = bannerRepository.countExposedBanners();
        // 만약에 배너 노출 개수가 줄어들었는데, 현재 노출 배너 개수보다 작다면
        if(exLimit > count && count < nowExposedBannerCount) {
            List<Banner> exposedBanners = bannerRepository.getExposedBanners();
            // order 기준 오름차순이라서 
            // 뒤에서부터 노출 비활성화 -> 노출순서가 낮으면 먼저 비노출됨
            for (int i = 1; i <= exLimit - count; i++) {
                exposedBanners.get(exposedBanners.size() - i).setOrder(null);
            }
            bannerRepository.saveAll(exposedBanners);
        }
        // 배너 노출 개수 갱신
        found.updateCount(count);
        
        return new CommonResDto(HttpStatus.OK, "노출 배너 개수 갱신됨", count);
    }

    // 배너 이미지를 저장하는 로직
    private String setThumbnailImage(MultipartFile imageFile) {
        String thumbnailImagePath = null;

        // null이거나 비어있는 파일이면 로직 처리 안함.
        if (imageFile != null && !imageFile.isEmpty()) {

            // 이미지 진위여부 검증
            ImageValidation.validateImageFile(imageFile);

            try {
                String originalFilename = imageFile.getOriginalFilename();

                // UUID + 원본 파일명으로 저장 (중복 방지)
                String fileName = UUID.randomUUID() + "_" + originalFilename;

                // s3 버킷에 이미지 저장하고 저장된 경로를 받아오기
                thumbnailImagePath = s3Config.uploadToS3Bucket(imageFile.getBytes(), fileName);
            } catch (IOException e) {
                // 저장 실패 처리
                e.printStackTrace();
                throw new CommonException(ErrorCode.FILE_SERVER_ERROR);
            }
        }
        return thumbnailImagePath;
    }

    // 배너가 노출될 수 있는지 확인하는 메소드
    private boolean isValidBanners() {

        Optional<BannerCount> foundBannerCount = bannerCountRepository.findById(1L);
        // 만일 배너 개수 DB가 초기화가 제대로 진행되지 않았다면
        if(!foundBannerCount.isPresent()) {
            // 초기화 시작
            init();
        }
        // 정해진 노출 배너 개수
        int bannerCount = foundBannerCount.get().getCount();
        
        // 현재 활성화 되어있고, 노출되고 있는 배너 개수
        Integer nowCount = bannerRepository.countExposedBanners();
        // 노출 가능
        if(nowCount < bannerCount) {
            return true;
        }
        // 현재 노출되고 있는 개수가 정해진 노출 개수보다 같거나 많다면
        else {
            return false;
        }
    }

    // 배너의 유효성을 검증 후 리턴해주는 메소드
    private Banner findBannerById(Long id) {
        Optional<Banner> byId = bannerRepository.findById(id);
        // 존재하지 않거나, 삭제된 배너인 경우
        if(!byId.isPresent() || !byId.get().isActive()) {
            throw new CommonException(ErrorCode.BAD_REQUEST, "해당 배너는 존재하지 않음.");
        }
        return byId.get();
    }

    // 배너 노출 순서 수정 시, order 값들, bannerId의 중복 여부를 확인하는 메소드
    private void validateUniqueBannerOrders(List<OrderModiReqDto> dtoList) {
        Set<Long> bannerIdSet = new HashSet<>();
        Set<Integer> orderSet = new HashSet<>();

        List<Integer> validNum = new ArrayList<>();
        Integer max = bannerCountRepository.findById(1L).get().getCount();
        for (int i = 1; i <= max; i++) {
            validNum.add(i);
        }

        for (OrderModiReqDto dto : dtoList) {
            // bannerId를 set에 넣음
            boolean bannerIdExists = !bannerIdSet.add(dto.getBannerId());
            // order를 set에 넣음
            boolean orderExists = !orderSet.add(dto.getOrder());
            // 1 ~ max값 까지의 정수 인지 확인
            boolean validOrder = !validNum.contains(dto.getOrder());

            // 만일 add가 실패하면 -> 중복 -> 유효하지 않은 요청 데이터임
            if (bannerIdExists || orderExists || validOrder) {
                throw new CommonException(ErrorCode.BAD_REQUEST);
            }
        }
    }

    // bannerCount 엔티티의 데이터 개수를 1개로 유지시키기 위한 로직임.
    // 혹시 모를 상황들을 대비 하기 위함.
    // 배너 노출 개수 수정 시, 데이터 개수가 2개 이상인지 확인하고, 제거해주는 로직
    private void validateBannerCount() {
        List<BannerCount> all = bannerCountRepository.findAll();
        // 데이터가 1개만 있어야 하는데, 2개 이상 존재하는 경우
        List<BannerCount> toDelete = null;
        if (all.size() > 1) {
            toDelete = all.subList(1, all.size());
        }
        // 첫번째 아이템을 제외한 모든 데이터 삭제
        if(toDelete != null) {
            bannerCountRepository.deleteAll(toDelete);
        }
    }

}
