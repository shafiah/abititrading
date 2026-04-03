package com.abiti_app_service.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "Payments")
@Table(name = "PAYMENTS")
@Getter
@Setter
public class Payments {
	
	private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ORDER_ID")
    private String orderId;

    @Column(name = "PAYMENT_ID")
    private String paymentId;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "AMOUNT")
    private Double amount;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;
}