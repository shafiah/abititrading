package com.abiti_app_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abiti_app_service.models.OtpEntity;

@Repository
public interface OtpRepository
        extends JpaRepository<OtpEntity, Long> {

    OtpEntity findByEmailId(String emailId);
}