package com.parking.controller;

import com.parking.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@RequestMapping("/manager/reports")
public class ReportController {
    @Autowired private FirebaseService firebaseService;

    @GetMapping
    public String reports(Model model) {
        try {
            // Revenue data
            List<Map<String, Object>> payments = firebaseService.getAll("payments");
            double totalRevenue = 0, cashRevenue = 0, cardRevenue = 0, walletRevenue = 0, bankRevenue = 0;
            int paidCount = 0, pendingCount = 0, refundedCount = 0;
            for (Map<String, Object> p : payments) {
                double amount = ((Number) p.getOrDefault("amount", 0)).doubleValue();
                String status = (String) p.getOrDefault("paymentStatus", "");
                String method = (String) p.getOrDefault("paymentMethod", "");
                if ("PAID".equals(status)) {
                    totalRevenue += amount; paidCount++;
                    switch (method) {
                        case "CASH": cashRevenue += amount; break;
                        case "CREDIT_CARD": cardRevenue += amount; break;
                        case "E_WALLET": walletRevenue += amount; break;
                        case "BANK_TRANSFER": bankRevenue += amount; break;
                    }
                } else if ("PENDING".equals(status)) pendingCount++;
                else if ("REFUNDED".equals(status)) refundedCount++;
            }

            // Session data
            List<Map<String, Object>> sessions = firebaseService.getAll("parkingSessions");
            int totalSessions = sessions.size(), activeSessions = 0, completedSessions = 0;
            Map<String, Integer> vehicleTypeCount = new HashMap<>();
            for (Map<String, Object> s : sessions) {
                String status = (String) s.getOrDefault("status", "");
                if ("PARKING".equals(status)) activeSessions++;
                else if ("COMPLETED".equals(status)) completedSessions++;
                String vType = (String) s.getOrDefault("vehicleTypeName", "Other");
                vehicleTypeCount.merge(vType, 1, Integer::sum);
            }

            // Slot data
            List<Map<String, Object>> slots = firebaseService.getAll("parkingSlots");
            int totalSlots = slots.size(), available = 0, occupied = 0, reserved = 0, maintenance = 0, locked = 0;
            for (Map<String, Object> s : slots) {
                switch ((String) s.getOrDefault("status", "")) {
                    case "AVAILABLE": available++; break;
                    case "OCCUPIED": occupied++; break;
                    case "RESERVED": reserved++; break;
                    case "MAINTENANCE": maintenance++; break;
                    case "LOCKED": locked++; break;
                }
            }

            // Incident data
            List<Map<String, Object>> incidents = firebaseService.getAll("incidentReports");
            int openIncidents = 0, inProgressIncidents = 0, resolvedIncidents = 0;
            Map<String, Integer> incidentTypeCount = new HashMap<>();
            for (Map<String, Object> i : incidents) {
                switch ((String) i.getOrDefault("status", "")) {
                    case "OPEN": openIncidents++; break;
                    case "IN_PROGRESS": inProgressIncidents++; break;
                    case "RESOLVED": case "CLOSED": resolvedIncidents++; break;
                }
                String type = (String) i.getOrDefault("incidentType", "OTHER");
                incidentTypeCount.merge(type, 1, Integer::sum);
            }

            model.addAttribute("totalRevenue", totalRevenue);
            model.addAttribute("cashRevenue", cashRevenue);
            model.addAttribute("cardRevenue", cardRevenue);
            model.addAttribute("walletRevenue", walletRevenue);
            model.addAttribute("bankRevenue", bankRevenue);
            model.addAttribute("paidCount", paidCount);
            model.addAttribute("pendingCount", pendingCount);
            model.addAttribute("refundedCount", refundedCount);
            model.addAttribute("totalSessions", totalSessions);
            model.addAttribute("activeSessions", activeSessions);
            model.addAttribute("completedSessions", completedSessions);
            model.addAttribute("vehicleTypeCount", vehicleTypeCount);
            model.addAttribute("totalSlots", totalSlots);
            model.addAttribute("availableSlots", available);
            model.addAttribute("occupiedSlots", occupied);
            model.addAttribute("reservedSlots", reserved);
            model.addAttribute("maintenanceSlots", maintenance);
            model.addAttribute("lockedSlots", locked);
            model.addAttribute("openIncidents", openIncidents);
            model.addAttribute("inProgressIncidents", inProgressIncidents);
            model.addAttribute("resolvedIncidents", resolvedIncidents);
            model.addAttribute("incidentTypeCount", incidentTypeCount);
            model.addAttribute("occupancyRate", totalSlots > 0 ? (occupied * 100.0 / totalSlots) : 0);

        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "reports/dashboard";
    }
}
