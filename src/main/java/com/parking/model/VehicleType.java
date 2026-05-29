package com.parking.model;

import java.util.HashMap;
import java.util.Map;

public class VehicleType {
    private String id;
    private String typeName;
    private String description;

    public VehicleType() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("typeName", typeName);
        map.put("description", description);
        return map;
    }

    public static VehicleType fromMap(String id, Map<String, Object> map) {
        VehicleType vt = new VehicleType();
        vt.setId(id);
        vt.setTypeName((String) map.getOrDefault("typeName", ""));
        vt.setDescription((String) map.getOrDefault("description", ""));
        return vt;
    }
}
