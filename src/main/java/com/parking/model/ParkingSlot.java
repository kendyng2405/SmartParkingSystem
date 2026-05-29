package com.parking.model;

import java.util.HashMap;
import java.util.Map;

public class ParkingSlot {
    private String id;
    private String zoneId;
    private String zoneName;
    private String slotCode;
    private String vehicleTypeId;
    private String vehicleTypeName;
    private String status; // AVAILABLE, OCCUPIED, RESERVED, MAINTENANCE, LOCKED
    private boolean isActive;

    public ParkingSlot() { this.isActive = true; this.status = "AVAILABLE"; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }
    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }
    public String getSlotCode() { return slotCode; }
    public void setSlotCode(String slotCode) { this.slotCode = slotCode; }
    public String getVehicleTypeId() { return vehicleTypeId; }
    public void setVehicleTypeId(String vehicleTypeId) { this.vehicleTypeId = vehicleTypeId; }
    public String getVehicleTypeName() { return vehicleTypeName; }
    public void setVehicleTypeName(String vehicleTypeName) { this.vehicleTypeName = vehicleTypeName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("zoneId", zoneId);
        map.put("zoneName", zoneName);
        map.put("slotCode", slotCode);
        map.put("vehicleTypeId", vehicleTypeId);
        map.put("vehicleTypeName", vehicleTypeName);
        map.put("status", status);
        map.put("isActive", isActive);
        return map;
    }

    public static ParkingSlot fromMap(String id, Map<String, Object> map) {
        ParkingSlot ps = new ParkingSlot();
        ps.setId(id);
        ps.setZoneId((String) map.getOrDefault("zoneId", ""));
        ps.setZoneName((String) map.getOrDefault("zoneName", ""));
        ps.setSlotCode((String) map.getOrDefault("slotCode", ""));
        ps.setVehicleTypeId((String) map.getOrDefault("vehicleTypeId", ""));
        ps.setVehicleTypeName((String) map.getOrDefault("vehicleTypeName", ""));
        ps.setStatus((String) map.getOrDefault("status", "AVAILABLE"));
        ps.setActive((Boolean) map.getOrDefault("isActive", true));
        return ps;
    }
}
