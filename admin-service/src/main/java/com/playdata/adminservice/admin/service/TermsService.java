package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.TermsInsertReqDto;
import com.playdata.adminservice.admin.dto.req.TermsUpdateReqDto;
import com.playdata.adminservice.admin.entity.Terms;
import com.playdata.adminservice.admin.entity.TermsCategory;
import com.playdata.adminservice.admin.repository.TermsRepository;
import com.playdata.adminservice.common.auth.TokenUserInfo;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import com.playdata.adminservice.common.exception.CommonException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

@Service
@Slf4j
@RequiredArgsConstructor
public class TermsService {

    private final TermsRepository termsRepository;

    /**
     * 약관/개인정보처리방침/QNA 등록
     *
     * @param userInfo
     * @param termsInsertReqDto
     * @return
     */
    public Terms insertTerms(TokenUserInfo userInfo, @PathVariable String category, @Valid TermsInsertReqDto termsInsertReqDto) {
        TermsCategory termsCategory = parseCategory(category);
        Long adminId = userInfo.getAdminId();
        return termsRepository.save(termsInsertReqDto.toEntity(adminId, termsCategory));
    }

    /**
     * 약관/개인정보처리방침/QNA 수정
     *
     * @param id
     * @param termsUpdateReqDto
     * @return
     */
    @Transactional
    public Terms updateTerms(@PathVariable Long id, @PathVariable String category, @Valid TermsUpdateReqDto termsUpdateReqDto) {
        parseCategory(category);
        Terms terms = termsRepository.findById(id).orElseThrow(
                () -> new CommonException(ErrorCode.DATA_NOT_FOUND)
        );
        terms.updateTerms(termsUpdateReqDto);
        return terms;
    }

    /**
     * 약관/개인정보처리방침/QNA 삭제
     *
     * @param id
     * @return
     */
    @Transactional
    public Terms deleteTerms(Long id, @PathVariable String category) {
        parseCategory(category);
        Terms terms = termsRepository.findById(id).orElseThrow(
                ()  -> new CommonException(ErrorCode.DATA_NOT_FOUND)
        );
        terms.deleteTerms();
        return terms;
    }

    /**
     * 주소로 들어온 값 Eunm 비교
     * @param category
     * @return
     */
    private TermsCategory parseCategory(String category) {
        try {
            return TermsCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
    }
}
