package com.playdata.adminservice.admin.repository;

import com.playdata.adminservice.admin.entity.Terms;
import com.playdata.adminservice.admin.repository.custom.TermsRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRepository extends JpaRepository<Terms, Long>, TermsRepositoryCustom {

    Optional<Terms> findByTermsIdAndActiveIsTrue(Long id);

}
