package com.parking.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Building {
    private String id;
    private String buildingName;
    private String address;
    private int totalFloors;
    private String operatingStartTime;
    private String operatingEndTime;
    private Date createdAt;

    public Building() { this.createdAt = new Date(); }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getBuildingName() { return buildingName; }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public int getTotalFloors() { return totalFloors; }
    public void setTotalFloors(int totalFloors) { this.totalFloors = totalFloors; }
    public String getOperatingStartTime() { return operatingStartTime; }
    public void setOperatingStartTime(String operatingStartTime) { this.operatingStartTime = operatingStartTime; }
    public String getOperatingEndTime() { return operatingEndTime; }
    public void setOperatingEndTime(String operatingEndTime) { this.operatingEndTime = operatingEndTime; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("buildingName", buildingName);
        map.put("address", address);
        map.put("totalFloors", totalFloors);
        map.put("operatingStartTime", operatingStartTime);
        map.put("operatingEndTime", operatingEndTime);
        map.put("createdAt", createdAt);
        return map;
    }

    public static Building fromMap(String id, Map<String, Object> map) {
        Building b = new Building();
        b.setId(id);
        b.setBuildingName((String) map.getOrDefault("buildingName", ""));
        b.setAddress((String) map.getOrDefault("address", ""));
        b.setTotalFloors(((Number) map.getOrDefault("totalFloors", 0)).intValue());
        b.setOperatingStartTime((String) map.getOrDefault("operatingStartTime", ""));
        b.setOperatingEndTime((String) map.getOrDefault("operatingEndTime", ""));
        return b;
    }
}
