package com.playdata.adminservice.admin.service;

import com.playdata.adminservice.admin.dto.req.AdminLogReqDto;
import com.playdata.adminservice.admin.dto.req.UserSearchDto;
import com.playdata.adminservice.admin.dto.res.UserDetailResDto;
import com.playdata.adminservice.admin.dto.res.UserListResDto;
import com.playdata.adminservice.admin.entity.User;
import com.playdata.adminservice.admin.repository.AdminLogRepository;
import com.playdata.adminservice.admin.repository.UserRepository;
import com.playdata.adminservice.common.auth.TokenAdminInfo;
import com.playdata.adminservice.common.enumeration.ErrorCode;
import com.playdata.adminservice.common.exception.CommonException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final AdminLogRepository adminLogRepository;

    /**
     * [관리자] - 사용자 목록 조회 (검색, 페이징)
     * @param searchDto
     * @param pageable
     * @return
     */
    public Page<UserListResDto> findUserList(UserSearchDto searchDto, Pageable pageable) {
        return userRepository.findList(searchDto, pageable);
    }

    /**
     * [관리자] - 사용자 상세 조회
     * @param id
     * @return
     */
    @Transactional
    public UserDetailResDto findUser(TokenAdminInfo adminInfo, Long id, HttpServletRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.DATA_NOT_FOUND));

        AdminLogReqDto logDto = new AdminLogReqDto(adminInfo.getAdminId(), user.getUserId(), getClientIp(request));
        adminLogRepository.save(logDto.toEntity());

        return new UserDetailResDto(user);
    }

    /**
     * 클라이언트의 실제 IP 주소를 추출하는 메서드
     * 프록시 서버(Nginx, AWS ELB 등)를 통과한 요청의 경우,
     * HttpServletRequest.getRemoteAddr()로는 실제 IP를 알 수 없으므로,
     * 다양한 헤더를 우선적으로 검사해 원본 클라이언트 IP를 얻는다.
     *
     * @param request 현재 요청 객체
     * @return 클라이언트의 실제 IP 주소
     */
    private String getClientIp(HttpServletRequest request) {
        // 일반적으로 많이 사용되는 클라이언트 IP 관련 HTTP 헤더들을 나열한 배열
        // 앞에 있는 항목일수록 우선적으로 검사
        final String[] headers = {
                "X-Forwarded-For",      // 가장 일반적인 프록시 환경에서 사용되는 헤더 (client, proxy1, proxy2 형태로 전달)
                "Proxy-Client-IP",      // 일부 프록시 서버(Apache 등)에서 사용
                "WL-Proxy-Client-IP",   // WebLogic에서 사용
                "HTTP_CLIENT_IP",       // 일부 서버 환경에서 사용
                "HTTP_X_FORWARDED_FOR", // 또 다른 변형된 포워딩 헤더
                "X-Real-IP"             // Nginx에서 자주 사용되는 클라이언트 IP 헤더
        };

        // 위의 헤더 목록을 순회하면서, 유효한 IP가 담긴 헤더를 찾는다
        for (String header : headers) {
            String ip = request.getHeader(header); // 현재 헤더 값 조회
            if (isValidIp(ip)) {
                // 여러 IP가 있을 경우, 첫 번째 IP가 실제 클라이언트의 IP이므로 추출
                return extractFirstIp(ip);
            }
        }

        // 위 헤더들에 유효한 값이 없을 경우, 최후의 수단으로 request.getRemoteAddr() 사용
        // 단, 이 경우 프록시 서버의 IP일 수 있으므로 정확도가 떨어질 수 있음
        return request.getRemoteAddr();
    }

    /**
     * 주어진 IP 문자열이 유효한지 검사하는 헬퍼 메서드
     *
     * @param ip 검사할 IP 문자열
     * @return null이 아니고, 비어있지 않으며, "unknown" 문자열이 아닌 경우 true
     */
    private boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip);
    }

    /**
     * X-Forwarded-For 같이 여러 IP가 쉼표로 구분된 경우,
     * 첫 번째 IP가 실제 클라이언트 IP이므로 그 값을 추출한다.
     *
     * 예: "123.123.123.123, 10.0.0.1, 127.0.0.1" → "123.123.123.123"
     *
     * @param ip 원본 IP 문자열
     * @return 첫 번째 IP 주소
     */
    private String extractFirstIp(String ip) {
        return ip.contains(",") ? ip.split(",")[0].trim() : ip.trim();
    }
}
