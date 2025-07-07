package com.playdata.userservice.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.playdata.userservice.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // email을 통한 회원 조회
    Optional<User> findByEmail(String email);


}
