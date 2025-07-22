package com.playdata.adminservice.admin.repository;

import com.playdata.adminservice.admin.entity.User;
import com.playdata.adminservice.admin.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

}
