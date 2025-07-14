package com.playdata.userservice.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.playdata.userservice.user.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // email을 통한 회원 조회
    Optional<User> findByEmail(String email);

    // 이메일, 또는 닉네임으로 회원 목록 조회
    @Query("SELECT u FROM User u WHERE u.active = true AND u.email = :keyword OR u.nickname = :keyword")
    Optional<List<User>> findByKeyword(String keyword);

    // 기존에 소셜 로그인한 사용자가 있는 지 확인
    @Query("SELECT u FROM User u WHERE u.socialProvider = :socialProvider " +
            "AND u.socialId = :socialId")
    Optional<User> findSocialUser(@Param("socialProvider") String socialProvider,
                                  @Param("socialId") String socialId);

}
