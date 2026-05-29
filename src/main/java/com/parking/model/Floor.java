package com.parking.model;

import java.util.HashMap;
import java.util.Map;

public class Floor {
    private String id;
    private String buildingId;
    private String buildingName;
    private int floorNumber;
    private String floorName;

    public Floor() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getBuildingId() { return buildingId; }
    public void setBuildingId(String buildingId) { this.buildingId = buildingId; }
    public String getBuildingName() { return buildingName; }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }
    public int getFloorNumber() { return floorNumber; }
    public void setFloorNumber(int floorNumber) { this.floorNumber = floorNumber; }
    public String getFloorName() { return floorName; }
    public void setFloorName(String floorName) { this.floorName = floorName; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("buildingId", buildingId);
        map.put("buildingName", buildingName);
        map.put("floorNumber", floorNumber);
        map.put("floorName", floorName);
        return map;
    }

    public static Floor fromMap(String id, Map<String, Object> map) {
        Floor f = new Floor();
        f.setId(id);
        f.setBuildingId((String) map.getOrDefault("buildingId", ""));
        f.setBuildingName((String) map.getOrDefault("buildingName", ""));
        f.setFloorNumber(((Number) map.getOrDefault("floorNumber", 0)).intValue());
        f.setFloorName((String) map.getOrDefault("floorName", ""));
        return f;
    }
}
