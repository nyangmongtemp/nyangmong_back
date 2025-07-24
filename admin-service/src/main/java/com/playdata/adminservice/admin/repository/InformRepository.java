package com.playdata.adminservice.admin.repository;

import com.playdata.adminservice.admin.entity.Inform;
import com.playdata.adminservice.admin.repository.custom.InformRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformRepository extends JpaRepository<Inform, Long>, InformRepositoryCustom {

}
