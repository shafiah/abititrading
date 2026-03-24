package com.abiti_app_service.models;

public class ResponseModel {

    private String error;
    private String message;

    // Constructor
    public ResponseModel(String error, String message) {
        this.error = error;
        this.message = message;
    }

    // Getter
    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}