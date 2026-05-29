package com.parking.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ParkingSession {
    private String id;
    private String vehicleId;
    private String licensePlate;
    private String vehicleTypeName;
    private String slotId;
    private String slotCode;
    private Date entryTime;
    private Date exitTime;
    private String entryGate;
    private String exitGate;
    private String status; // PARKING, COMPLETED, LOST_TICKET, UNPAID, VIOLATION
    private double estimatedFee;
    private double finalFee;
    private String createdBy;
    private String createdByName;

    public ParkingSession() { this.entryTime = new Date(); this.status = "PARKING"; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public String getVehicleTypeName() { return vehicleTypeName; }
    public void setVehicleTypeName(String vehicleTypeName) { this.vehicleTypeName = vehicleTypeName; }
    public String getSlotId() { return slotId; }
    public void setSlotId(String slotId) { this.slotId = slotId; }
    public String getSlotCode() { return slotCode; }
    public void setSlotCode(String slotCode) { this.slotCode = slotCode; }
    public Date getEntryTime() { return entryTime; }
    public void setEntryTime(Date entryTime) { this.entryTime = entryTime; }
    public Date getExitTime() { return exitTime; }
    public void setExitTime(Date exitTime) { this.exitTime = exitTime; }
    public String getEntryGate() { return entryGate; }
    public void setEntryGate(String entryGate) { this.entryGate = entryGate; }
    public String getExitGate() { return exitGate; }
    public void setExitGate(String exitGate) { this.exitGate = exitGate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getEstimatedFee() { return estimatedFee; }
    public void setEstimatedFee(double estimatedFee) { this.estimatedFee = estimatedFee; }
    public double getFinalFee() { return finalFee; }
    public void setFinalFee(double finalFee) { this.finalFee = finalFee; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("vehicleId", vehicleId);
        map.put("licensePlate", licensePlate);
        map.put("vehicleTypeName", vehicleTypeName);
        map.put("slotId", slotId);
        map.put("slotCode", slotCode);
        map.put("entryTime", entryTime);
        map.put("exitTime", exitTime);
        map.put("entryGate", entryGate);
        map.put("exitGate", exitGate);
        map.put("status", status);
        map.put("estimatedFee", estimatedFee);
        map.put("finalFee", finalFee);
        map.put("createdBy", createdBy);
        map.put("createdByName", createdByName);
        return map;
    }

    public static ParkingSession fromMap(String id, Map<String, Object> map) {
        ParkingSession ps = new ParkingSession();
        ps.setId(id);
        ps.setVehicleId((String) map.getOrDefault("vehicleId", ""));
        ps.setLicensePlate((String) map.getOrDefault("licensePlate", ""));
        ps.setVehicleTypeName((String) map.getOrDefault("vehicleTypeName", ""));
        ps.setSlotId((String) map.getOrDefault("slotId", ""));
        ps.setSlotCode((String) map.getOrDefault("slotCode", ""));
        ps.setEntryGate((String) map.getOrDefault("entryGate", ""));
        ps.setExitGate((String) map.getOrDefault("exitGate", ""));
        ps.setStatus((String) map.getOrDefault("status", "PARKING"));
        ps.setEstimatedFee(((Number) map.getOrDefault("estimatedFee", 0.0)).doubleValue());
        ps.setFinalFee(((Number) map.getOrDefault("finalFee", 0.0)).doubleValue());
        ps.setCreatedBy((String) map.getOrDefault("createdBy", ""));
        ps.setCreatedByName((String) map.getOrDefault("createdByName", ""));
        Object entry = map.get("entryTime");
        if (entry instanceof com.google.cloud.Timestamp) ps.setEntryTime(((com.google.cloud.Timestamp) entry).toDate());
        Object exit = map.get("exitTime");
        if (exit instanceof com.google.cloud.Timestamp) ps.setExitTime(((com.google.cloud.Timestamp) exit).toDate());
        return ps;
    }
}
