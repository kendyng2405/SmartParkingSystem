package com.parking.model;

import java.util.HashMap;
import java.util.Map;

public class Vehicle {
    private String id;
    private String licensePlate;
    private String vehicleTypeId;
    private String vehicleTypeName;
    private String ownerName;
    private String ownerPhone;

    public Vehicle() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public String getVehicleTypeId() { return vehicleTypeId; }
    public void setVehicleTypeId(String vehicleTypeId) { this.vehicleTypeId = vehicleTypeId; }
    public String getVehicleTypeName() { return vehicleTypeName; }
    public void setVehicleTypeName(String vehicleTypeName) { this.vehicleTypeName = vehicleTypeName; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public String getOwnerPhone() { return ownerPhone; }
    public void setOwnerPhone(String ownerPhone) { this.ownerPhone = ownerPhone; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("licensePlate", licensePlate);
        map.put("vehicleTypeId", vehicleTypeId);
        map.put("vehicleTypeName", vehicleTypeName);
        map.put("ownerName", ownerName);
        map.put("ownerPhone", ownerPhone);
        return map;
    }

    public static Vehicle fromMap(String id, Map<String, Object> map) {
        Vehicle v = new Vehicle();
        v.setId(id);
        v.setLicensePlate((String) map.getOrDefault("licensePlate", ""));
        v.setVehicleTypeId((String) map.getOrDefault("vehicleTypeId", ""));
        v.setVehicleTypeName((String) map.getOrDefault("vehicleTypeName", ""));
        v.setOwnerName((String) map.getOrDefault("ownerName", ""));
        v.setOwnerPhone((String) map.getOrDefault("ownerPhone", ""));
        return v;
    }
}
