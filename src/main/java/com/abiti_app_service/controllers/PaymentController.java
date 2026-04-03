package com.abiti_app_service.controllers;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@RestController
@RequestMapping("/user/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

	@GetMapping("/testpayemnt")
	public String testingPayment() {
		return "congrats payment successfully done";
	}
	
	private final RazorpayClient razorpayClient;

    public PaymentController(RazorpayClient razorpayClient) {
        this.razorpayClient = razorpayClient;
    }

    @PostMapping("/create-order")
    public String createOrder() throws Exception {

        JSONObject options = new JSONObject();
        options.put("amount", 1 * 100); // ₹5000 → paise
        options.put("currency", "INR");
        options.put("receipt", "txn_001");

        Order order = razorpayClient.orders.create(options);

        return order.toString(); // JSON response
    }
    
 // ===============================
 // ⭐ NEW API: VERIFY PAYMENT
 // ===============================
 @PostMapping("/verify")
 public ResponseEntity<String> verifyPayment(@RequestParam String paymentId) {

     try {

         // 🔥 FETCH PAYMENT FROM RAZORPAY
         com.razorpay.Payment payment = razorpayClient.payments.fetch(paymentId);

         String status = payment.get("status");

         // ✅ SUCCESS CHECK
         if ("captured".equals(status)) {

             // ⭐ TODO: USER PRIME UPDATE (next step)
             System.out.println("Payment Verified Successfully");

             return ResponseEntity.ok("Payment Verified");

         } else {
             return ResponseEntity.badRequest().body("Payment Not Captured");
         }

     } catch (Exception e) {
         e.printStackTrace();
         return ResponseEntity.status(500).body("Verification Failed");
     }
 }
}
