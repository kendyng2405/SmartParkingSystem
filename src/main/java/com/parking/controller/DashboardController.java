package com.parking.controller;

import com.parking.model.User;
import com.parking.service.CustomUserDetailsService;
import com.parking.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class DashboardController {

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        try {
            User user = userDetailsService.getUserByEmail(auth.getName());
            model.addAttribute("currentUser", user);
            model.addAttribute("roleName", user != null ? user.getRoleName() : "DRIVER");

            // Get statistics
            long totalSlots = firebaseService.count("parkingSlots");
            long occupiedSlots = firebaseService.countByField("parkingSlots", "status", "OCCUPIED");
            long availableSlots = firebaseService.countByField("parkingSlots", "status", "AVAILABLE");
            long reservedSlots = firebaseService.countByField("parkingSlots", "status", "RESERVED");
            long activeSessions = firebaseService.countByField("parkingSessions", "status", "PARKING");
            long totalBuildings = firebaseService.count("buildings");
            long totalUsers = firebaseService.count("users");
            long totalPayments = firebaseService.count("payments");
            long openIncidents = firebaseService.countByField("incidentReports", "status", "OPEN");

            // Calculate revenue
            double totalRevenue = 0;
            List<Map<String, Object>> payments = firebaseService.getByField("payments", "paymentStatus", "PAID");
            for (Map<String, Object> p : payments) {
                totalRevenue += ((Number) p.getOrDefault("amount", 0.0)).doubleValue();
            }

            model.addAttribute("totalSlots", totalSlots);
            model.addAttribute("occupiedSlots", occupiedSlots);
            model.addAttribute("availableSlots", availableSlots);
            model.addAttribute("reservedSlots", reservedSlots);
            model.addAttribute("activeSessions", activeSessions);
            model.addAttribute("totalBuildings", totalBuildings);
            model.addAttribute("totalUsers", totalUsers);
            model.addAttribute("totalPayments", totalPayments);
            model.addAttribute("openIncidents", openIncidents);
            model.addAttribute("totalRevenue", String.format("%.2f", totalRevenue));
            model.addAttribute("occupancyRate", totalSlots > 0 ? (occupiedSlots * 100 / totalSlots) : 0);

            // Recent sessions
            List<Map<String, Object>> recentSessions = firebaseService.getAll("parkingSessions");
            if (recentSessions.size() > 10) recentSessions = recentSessions.subList(recentSessions.size() - 10, recentSessions.size());
            Collections.reverse(recentSessions);
            model.addAttribute("recentSessions", recentSessions);

        } catch (Exception e) {
            model.addAttribute("error", "Error loading dashboard: " + e.getMessage());
        }
        return "dashboard";
    }
}
