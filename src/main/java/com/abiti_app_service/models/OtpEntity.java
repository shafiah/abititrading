package com.abiti_app_service.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "OTP_DETAILS")
@Getter
@Setter
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMAIL_ID")
    private String emailId;

    @Column(name = "OTP")
    private String otp;

    @Column(name = "EXPIRY_TIME")
    private LocalDateTime expiryTime;

    @Column(name = "VERIFIED")
    private boolean verified;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
}