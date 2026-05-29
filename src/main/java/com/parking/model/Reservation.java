package com.parking.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Reservation {
    private String id;
    private String userId;
    private String userName;
    private String vehicleTypeId;
    private String vehicleTypeName;
    private String slotId;
    private String slotCode;
    private Date reservationStart;
    private Date reservationEnd;
    private String status; // PENDING, CONFIRMED, CANCELLED, EXPIRED
    private Date createdAt;

    public Reservation() { this.createdAt = new Date(); this.status = "PENDING"; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getVehicleTypeId() { return vehicleTypeId; }
    public void setVehicleTypeId(String vehicleTypeId) { this.vehicleTypeId = vehicleTypeId; }
    public String getVehicleTypeName() { return vehicleTypeName; }
    public void setVehicleTypeName(String vehicleTypeName) { this.vehicleTypeName = vehicleTypeName; }
    public String getSlotId() { return slotId; }
    public void setSlotId(String slotId) { this.slotId = slotId; }
    public String getSlotCode() { return slotCode; }
    public void setSlotCode(String slotCode) { this.slotCode = slotCode; }
    public Date getReservationStart() { return reservationStart; }
    public void setReservationStart(Date reservationStart) { this.reservationStart = reservationStart; }
    public Date getReservationEnd() { return reservationEnd; }
    public void setReservationEnd(Date reservationEnd) { this.reservationEnd = reservationEnd; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("userName", userName);
        map.put("vehicleTypeId", vehicleTypeId);
        map.put("vehicleTypeName", vehicleTypeName);
        map.put("slotId", slotId);
        map.put("slotCode", slotCode);
        map.put("reservationStart", reservationStart);
        map.put("reservationEnd", reservationEnd);
        map.put("status", status);
        map.put("createdAt", createdAt);
        return map;
    }

    public static Reservation fromMap(String id, Map<String, Object> map) {
        Reservation r = new Reservation();
        r.setId(id);
        r.setUserId((String) map.getOrDefault("userId", ""));
        r.setUserName((String) map.getOrDefault("userName", ""));
        r.setVehicleTypeId((String) map.getOrDefault("vehicleTypeId", ""));
        r.setVehicleTypeName((String) map.getOrDefault("vehicleTypeName", ""));
        r.setSlotId((String) map.getOrDefault("slotId", ""));
        r.setSlotCode((String) map.getOrDefault("slotCode", ""));
        r.setStatus((String) map.getOrDefault("status", "PENDING"));
        Object start = map.get("reservationStart");
        if (start instanceof com.google.cloud.Timestamp) r.setReservationStart(((com.google.cloud.Timestamp) start).toDate());
        Object end = map.get("reservationEnd");
        if (end instanceof com.google.cloud.Timestamp) r.setReservationEnd(((com.google.cloud.Timestamp) end).toDate());
        return r;
    }
}
