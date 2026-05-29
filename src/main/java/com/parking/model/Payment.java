package com.parking.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Payment {
    private String id;
    private String sessionId;
    private String licensePlate;
    private double amount;
    private String paymentMethod; // CASH, BANK_TRANSFER, E_WALLET, CREDIT_CARD
    private String paymentStatus; // PENDING, PAID, FAILED, REFUNDED
    private Date paidAt;

    public Payment() { this.paymentStatus = "PENDING"; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public Date getPaidAt() { return paidAt; }
    public void setPaidAt(Date paidAt) { this.paidAt = paidAt; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", sessionId);
        map.put("licensePlate", licensePlate);
        map.put("amount", amount);
        map.put("paymentMethod", paymentMethod);
        map.put("paymentStatus", paymentStatus);
        map.put("paidAt", paidAt);
        return map;
    }

    public static Payment fromMap(String id, Map<String, Object> map) {
        Payment p = new Payment();
        p.setId(id);
        p.setSessionId((String) map.getOrDefault("sessionId", ""));
        p.setLicensePlate((String) map.getOrDefault("licensePlate", ""));
        p.setAmount(((Number) map.getOrDefault("amount", 0.0)).doubleValue());
        p.setPaymentMethod((String) map.getOrDefault("paymentMethod", ""));
        p.setPaymentStatus((String) map.getOrDefault("paymentStatus", "PENDING"));
        Object paid = map.get("paidAt");
        if (paid instanceof com.google.cloud.Timestamp) p.setPaidAt(((com.google.cloud.Timestamp) paid).toDate());
        return p;
    }
}
