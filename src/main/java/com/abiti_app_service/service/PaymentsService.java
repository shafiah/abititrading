package com.abiti_app_service.service;

import com.abiti_app_service.models.Payments;

public interface PaymentsService {

    Payments savePayment(Payments payment);

    Payments updatePayment(String orderId, String paymentId, String status);
}