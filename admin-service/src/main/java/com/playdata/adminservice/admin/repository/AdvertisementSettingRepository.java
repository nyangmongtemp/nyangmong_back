package com.playdata.adminservice.admin.repository;

import com.playdata.adminservice.admin.entity.AdvertisementCount;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdvertisementSettingRepository extends JpaRepository<AdvertisementCount, Long> {
    Optional<AdvertisementCount> findTopByOrderByAdNumIdDesc();
}