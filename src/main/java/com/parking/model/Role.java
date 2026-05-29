package com.parking.model;

import java.util.HashMap;
import java.util.Map;

public class Role {
    private String id;
    private String roleName;
    private String description;

    public Role() {}

    public Role(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("roleName", roleName);
        map.put("description", description);
        return map;
    }

    public static Role fromMap(String id, Map<String, Object> map) {
        Role role = new Role();
        role.setId(id);
        role.setRoleName((String) map.getOrDefault("roleName", ""));
        role.setDescription((String) map.getOrDefault("description", ""));
        return role;
    }
}
