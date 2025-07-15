package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.AdminLoginReqDto;
import com.playdata.adminservice.admin.entity.Admin;
import com.playdata.adminservice.admin.repository.AdminRepository;
import com.playdata.adminservice.common.auth.JwtTokenProvider;
import com.playdata.adminservice.common.dto.CommonResDto;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import com.playdata.adminservice.common.exception.CommonException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;

    // 비밀번호 인코딩용
    private final PasswordEncoder passwordEncoder;

    // 로그인 토큰 발급용
    private final JwtTokenProvider jwtTokenProvider;





    // 로그인
    public CommonResDto login(AdminLoginReqDto adminLoginReqDto) {

        // Admin email 조회
        Optional<Admin> findAdmin = adminRepository.findByEmail(adminLoginReqDto.getEmail());

        if(!findAdmin.isPresent()) { // email 정보가 없다면 회원가입 x
            throw new EntityNotFoundException("회원가입이 되지 않은 이메일입니다.");
        } else  {
            String password = adminLoginReqDto.getPassword();

            // 탈퇴한 관리자면 에러
            if (!findAdmin.get().isActive()) {
                throw new CommonException(ErrorCode.ACCOUNT_DISABLED);
            }
            // 비밀번호가 일치 하지 않는 경우
            if(!passwordEncoder.matches(adminLoginReqDto.getPassword(), password)) {
                throw new CommonException(ErrorCode.INVALID_PASSWORD);
            } else { // 유효한 관리자 이고, 비밀번호도 일치 한다.
                Admin admin = findAdmin.get();

                // access token 발급

            }


        }

    }
}
