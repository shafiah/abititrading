//package com.abiti_app_service.util;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//
//public class PaymentUtil {
//
//    public static String generateSignature(String data, String secret) throws Exception {
//
//        Mac mac = Mac.getInstance("HmacSHA256");
//        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
//
//        mac.init(secretKey);
//
//        byte[] hash = mac.doFinal(data.getBytes());
//
//        StringBuilder hex = new StringBuilder();
//        for (byte b : hash) {
//            hex.append(String.format("%02x", b));
//        }
//
//        return hex.toString();
//    }
//}