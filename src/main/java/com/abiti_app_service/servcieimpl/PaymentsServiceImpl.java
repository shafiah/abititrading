package com.abiti_app_service.servcieimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abiti_app_service.dao.PaymentsDao;
import com.abiti_app_service.models.Payments;
import com.abiti_app_service.service.PaymentsService;

import java.time.LocalDateTime;

@Service
public class PaymentsServiceImpl implements PaymentsService {

    @Autowired
    private PaymentsDao paymentsDao;

    @Override
    public Payments savePayment(Payments payment) {
        payment.setCreatedAt(LocalDateTime.now());
        return paymentsDao.save(payment);
    }

    @Override
    public Payments updatePayment(String orderId, String paymentId, String status) {

        Payments payment = paymentsDao.findByOrderId(orderId);

        if (payment != null) {
            payment.setPaymentId(paymentId);
            payment.setStatus(status);
            return paymentsDao.save(payment);
        }

        return null;
    }
}