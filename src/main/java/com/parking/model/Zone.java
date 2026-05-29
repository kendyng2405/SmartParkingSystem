package com.parking.model;

import java.util.HashMap;
import java.util.Map;

public class Zone {
    private String id;
    private String floorId;
    private String floorName;
    private String zoneName;
    private String description;

    public Zone() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFloorId() { return floorId; }
    public void setFloorId(String floorId) { this.floorId = floorId; }
    public String getFloorName() { return floorName; }
    public void setFloorName(String floorName) { this.floorName = floorName; }
    public String getZoneName() { return zoneName; }
    public void setZoneName(String zoneName) { this.zoneName = zoneName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("floorId", floorId);
        map.put("floorName", floorName);
        map.put("zoneName", zoneName);
        map.put("description", description);
        return map;
    }

    public static Zone fromMap(String id, Map<String, Object> map) {
        Zone z = new Zone();
        z.setId(id);
        z.setFloorId((String) map.getOrDefault("floorId", ""));
        z.setFloorName((String) map.getOrDefault("floorName", ""));
        z.setZoneName((String) map.getOrDefault("zoneName", ""));
        z.setDescription((String) map.getOrDefault("description", ""));
        return z;
    }
}
