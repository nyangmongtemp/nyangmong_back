package com.playdata.adminservice.admin.repository;

import com.playdata.adminservice.admin.entity.BannerCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerCountRepository extends JpaRepository<BannerCount, Long> {
}
