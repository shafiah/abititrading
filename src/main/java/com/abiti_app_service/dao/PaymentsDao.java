package com.abiti_app_service.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.abiti_app_service.models.Payments;

@Repository
public interface PaymentsDao extends JpaRepository<Payments, Long> {

    Payments findByOrderId(String orderId);
    Payments findByPaymentId(String paymentId);
}