package com.parking.model;

import java.util.HashMap;
import java.util.Map;

public class PricingPolicy {
    private String id;
    private String vehicleTypeId;
    private String vehicleTypeName;
    private String policyName;
    private double basePrice;
    private double pricePerHour;
    private double maxDailyRate;
    private double lostTicketFee;
    private double overtimeFeePerHour;
    private String effectiveFrom;
    private String effectiveTo;

    public PricingPolicy() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getVehicleTypeId() { return vehicleTypeId; }
    public void setVehicleTypeId(String vehicleTypeId) { this.vehicleTypeId = vehicleTypeId; }
    public String getVehicleTypeName() { return vehicleTypeName; }
    public void setVehicleTypeName(String vehicleTypeName) { this.vehicleTypeName = vehicleTypeName; }
    public String getPolicyName() { return policyName; }
    public void setPolicyName(String policyName) { this.policyName = policyName; }
    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public double getPricePerHour() { return pricePerHour; }
    public void setPricePerHour(double pricePerHour) { this.pricePerHour = pricePerHour; }
    public double getMaxDailyRate() { return maxDailyRate; }
    public void setMaxDailyRate(double maxDailyRate) { this.maxDailyRate = maxDailyRate; }
    public double getLostTicketFee() { return lostTicketFee; }
    public void setLostTicketFee(double lostTicketFee) { this.lostTicketFee = lostTicketFee; }
    public double getOvertimeFeePerHour() { return overtimeFeePerHour; }
    public void setOvertimeFeePerHour(double overtimeFeePerHour) { this.overtimeFeePerHour = overtimeFeePerHour; }
    public String getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(String effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public String getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(String effectiveTo) { this.effectiveTo = effectiveTo; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("vehicleTypeId", vehicleTypeId);
        map.put("vehicleTypeName", vehicleTypeName);
        map.put("policyName", policyName);
        map.put("basePrice", basePrice);
        map.put("pricePerHour", pricePerHour);
        map.put("maxDailyRate", maxDailyRate);
        map.put("lostTicketFee", lostTicketFee);
        map.put("overtimeFeePerHour", overtimeFeePerHour);
        map.put("effectiveFrom", effectiveFrom);
        map.put("effectiveTo", effectiveTo);
        return map;
    }

    public static PricingPolicy fromMap(String id, Map<String, Object> map) {
        PricingPolicy pp = new PricingPolicy();
        pp.setId(id);
        pp.setVehicleTypeId((String) map.getOrDefault("vehicleTypeId", ""));
        pp.setVehicleTypeName((String) map.getOrDefault("vehicleTypeName", ""));
        pp.setPolicyName((String) map.getOrDefault("policyName", ""));
        pp.setBasePrice(((Number) map.getOrDefault("basePrice", 0.0)).doubleValue());
        pp.setPricePerHour(((Number) map.getOrDefault("pricePerHour", 0.0)).doubleValue());
        pp.setMaxDailyRate(((Number) map.getOrDefault("maxDailyRate", 0.0)).doubleValue());
        pp.setLostTicketFee(((Number) map.getOrDefault("lostTicketFee", 0.0)).doubleValue());
        pp.setOvertimeFeePerHour(((Number) map.getOrDefault("overtimeFeePerHour", 0.0)).doubleValue());
        pp.setEffectiveFrom((String) map.getOrDefault("effectiveFrom", ""));
        pp.setEffectiveTo((String) map.getOrDefault("effectiveTo", ""));
        return pp;
    }
}
