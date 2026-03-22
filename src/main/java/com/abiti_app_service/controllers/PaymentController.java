package com.abiti_app_service.controllers;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
