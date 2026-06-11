package com.abiti_app_service.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {

    private String emailId;

    private String otp;

    private String newPassword;
}