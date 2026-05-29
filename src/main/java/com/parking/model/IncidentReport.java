package com.parking.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IncidentReport {
    private String id;
    private String sessionId;
    private String reportedBy;
    private String reportedByName;
    private String incidentType; // LOST_TICKET, WRONG_LICENSE_PLATE, OVERTIME, WRONG_ZONE, UNPAID, SLOT_OCCUPIED, OTHER
    private String description;
    private String status; // OPEN, IN_PROGRESS, RESOLVED, CLOSED
    private Date createdAt;

    public IncidentReport() { this.createdAt = new Date(); this.status = "OPEN"; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getReportedBy() { return reportedBy; }
    public void setReportedBy(String reportedBy) { this.reportedBy = reportedBy; }
    public String getReportedByName() { return reportedByName; }
    public void setReportedByName(String reportedByName) { this.reportedByName = reportedByName; }
    public String getIncidentType() { return incidentType; }
    public void setIncidentType(String incidentType) { this.incidentType = incidentType; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("sessionId", sessionId);
        map.put("reportedBy", reportedBy);
        map.put("reportedByName", reportedByName);
        map.put("incidentType", incidentType);
        map.put("description", description);
        map.put("status", status);
        map.put("createdAt", createdAt);
        return map;
    }

    public static IncidentReport fromMap(String id, Map<String, Object> map) {
        IncidentReport ir = new IncidentReport();
        ir.setId(id);
        ir.setSessionId((String) map.getOrDefault("sessionId", ""));
        ir.setReportedBy((String) map.getOrDefault("reportedBy", ""));
        ir.setReportedByName((String) map.getOrDefault("reportedByName", ""));
        ir.setIncidentType((String) map.getOrDefault("incidentType", ""));
        ir.setDescription((String) map.getOrDefault("description", ""));
        ir.setStatus((String) map.getOrDefault("status", "OPEN"));
        Object created = map.get("createdAt");
        if (created instanceof com.google.cloud.Timestamp) ir.setCreatedAt(((com.google.cloud.Timestamp) created).toDate());
        return ir;
    }
}
