package com.abiti_app_service.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abiti_app_service.models.Payments;
import com.abiti_app_service.models.Users;
import com.abiti_app_service.service.PaymentsService;
import com.abiti_app_service.service.UsersServcie;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@RestController
@RequestMapping("/user/payment")
@CrossOrigin(origins = "*")
public class PaymentController {
	
	@Autowired
	private PaymentsService paymentsService;

	@Autowired
	private UsersServcie usersServcie;

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
    public ResponseEntity<String> verifyPayment(
            @RequestParam String paymentId,
            @RequestParam String orderId,
          //  @RequestParam(required = false) String signature,    
            @RequestParam String phoneNumber   
    ) {

        try {
        	
//        	// 🔥 STEP 1: SIGNATURE VERIFY
//            String data = orderId + "|" + paymentId;
//
//            String generatedSignature = PaymentUtil.generateSignature(
//                    data,
//                    "uiV9Tb0rrnjDr8uVxwOWMzmw" // 🔥 same as application.properties
//            );
//
//            if (!generatedSignature.equals(signature)) {
//                return ResponseEntity.badRequest().body("Invalid Signature");
//            }


            // 🔥 FETCH PAYMENT FROM RAZORPAY
            com.razorpay.Payment payment = razorpayClient.payments.fetch(paymentId);
         // 🔥 DUPLICATE CHECK (ADD THIS)
            Payments existingPayment = paymentsService.findByPaymentId(paymentId);

            if (existingPayment != null) {
                return ResponseEntity.ok("Already Verified");
            }

            String status = payment.get("status");

            // ✅ SUCCESS CHECK
            if ("captured".equals(status)) {

                // ⭐ EXISTING LOG (UNCHANGED)
                System.out.println("Payment Verified Successfully");

                // =====================================================
                // 🔥 NEW CODE START (PAYMENT SAVE + USER PRIME UPDATE)
                // =====================================================

                // 🔥 1. SAVE PAYMENT IN DB
                Payments pay = new Payments();
                pay.setPaymentId(paymentId);
                pay.setOrderId(orderId);
                pay.setPhoneNumber(phoneNumber);

                // 🔥 amount paise me hota hai → rupees me convert
                if (payment.get("amount") != null) {
                    double amount = Double.parseDouble(payment.get("amount").toString()) / 100;
                    pay.setAmount(amount);
                }

                pay.setStatus("SUCCESS");

                paymentsService.savePayment(pay);

                // 🔥 2. UPDATE USER PRIME = TRUE
                Users user = usersServcie.findByPhoneNumber(phoneNumber);

                if (user != null) {
                   // user.setPrime(true);
                    usersServcie.updateUser(user.getId(), true);
                }

                // =====================================================
                // 🔥 NEW CODE END
                // =====================================================

                return ResponseEntity.ok("Payment Verified");

            } else {

                // =====================================================
                // 🔥 NEW CODE START (FAILED PAYMENT SAVE)
                // =====================================================

                Payments pay = new Payments();
                pay.setPaymentId(paymentId);
                pay.setOrderId(orderId);
                pay.setPhoneNumber(phoneNumber);
                pay.setStatus("FAILED");

                paymentsService.savePayment(pay);

                // =====================================================
                // 🔥 NEW CODE END
                // =====================================================

                return ResponseEntity.badRequest().body("Payment Not Captured");
            }

        } catch (Exception e) {
            e.printStackTrace();

            // =====================================================
            // 🔥 NEW CODE START (ERROR PAYMENT SAVE)
            // =====================================================

            Payments pay = new Payments();
            pay.setPaymentId(paymentId);
            pay.setOrderId(orderId);
            pay.setPhoneNumber(phoneNumber);
            pay.setStatus("ERROR");

            paymentsService.savePayment(pay);

            // =====================================================
            // 🔥 NEW CODE END
            // =====================================================

            return ResponseEntity.status(500).body("Verification Failed");
        }
    }
}
