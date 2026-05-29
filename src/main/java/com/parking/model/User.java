package com.parking.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String passwordHash;
    private String roleId;
    private String roleName;
    private boolean isActive;
    private Date createdAt;

    public User() {
        this.isActive = true;
        this.createdAt = new Date();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRoleId() { return roleId; }
    public void setRoleId(String roleId) { this.roleId = roleId; }
    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("fullName", fullName);
        map.put("email", email);
        map.put("phoneNumber", phoneNumber);
        map.put("passwordHash", passwordHash);
        map.put("roleId", roleId);
        map.put("roleName", roleName);
        map.put("isActive", isActive);
        map.put("createdAt", createdAt);
        return map;
    }

    public static User fromMap(String id, Map<String, Object> map) {
        User user = new User();
        user.setId(id);
        user.setFullName((String) map.getOrDefault("fullName", ""));
        user.setEmail((String) map.getOrDefault("email", ""));
        user.setPhoneNumber((String) map.getOrDefault("phoneNumber", ""));
        user.setPasswordHash((String) map.getOrDefault("passwordHash", ""));
        user.setRoleId((String) map.getOrDefault("roleId", ""));
        user.setRoleName((String) map.getOrDefault("roleName", ""));
        user.setActive((Boolean) map.getOrDefault("isActive", true));
        Object createdAt = map.get("createdAt");
        if (createdAt instanceof com.google.cloud.Timestamp) {
            user.setCreatedAt(((com.google.cloud.Timestamp) createdAt).toDate());
        } else if (createdAt instanceof Date) {
            user.setCreatedAt((Date) createdAt);
        }
        return user;
    }
}
