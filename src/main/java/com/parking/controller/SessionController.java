package com.parking.controller;

import com.parking.model.User;
import com.parking.service.CustomUserDetailsService;
import com.parking.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Controller
@RequestMapping("/staff/sessions")
public class SessionController {
    @Autowired private FirebaseService firebaseService;
    @Autowired private CustomUserDetailsService userService;

    @GetMapping
    public String list(@RequestParam(required = false) String status, Model model) {
        try {
            List<Map<String, Object>> sessions;
            if (status != null && !status.isEmpty()) {
                sessions = firebaseService.getByField("parkingSessions", "status", status);
            } else {
                sessions = firebaseService.getAll("parkingSessions");
            }
            Collections.reverse(sessions);
            model.addAttribute("sessions", sessions);
            model.addAttribute("filterStatus", status);
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "sessions/list";
    }

    @GetMapping("/start")
    public String startForm(Model model) {
        try {
            model.addAttribute("vehicleTypes", firebaseService.getAll("vehicleTypes"));
            model.addAttribute("availableSlots", firebaseService.getByField("parkingSlots", "status", "AVAILABLE"));
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "sessions/start";
    }

    @PostMapping("/start")
    public String startSession(@RequestParam String licensePlate, @RequestParam String vehicleTypeName,
                               @RequestParam String slotId, @RequestParam String slotCode,
                               @RequestParam String entryGate, Authentication auth, RedirectAttributes ra) {
        try {
            User user = userService.getUserByEmail(auth.getName());
            // Check or create vehicle
            List<Map<String, Object>> vehicles = firebaseService.getByField("vehicles", "licensePlate", licensePlate);
            String vehicleId;
            if (vehicles.isEmpty()) {
                Map<String, Object> vehicle = new HashMap<>();
                vehicle.put("licensePlate", licensePlate);
                vehicle.put("vehicleTypeName", vehicleTypeName);
                vehicleId = firebaseService.save("vehicles", vehicle);
            } else {
                vehicleId = (String) vehicles.get(0).get("id");
            }
            // Create session
            Map<String, Object> session = new HashMap<>();
            session.put("vehicleId", vehicleId); session.put("licensePlate", licensePlate);
            session.put("vehicleTypeName", vehicleTypeName); session.put("slotId", slotId);
            session.put("slotCode", slotCode); session.put("entryTime", new Date());
            session.put("entryGate", entryGate); session.put("status", "PARKING");
            session.put("estimatedFee", 0.0); session.put("finalFee", 0.0);
            session.put("createdBy", user != null ? user.getId() : "");
            session.put("createdByName", user != null ? user.getFullName() : "");
            firebaseService.save("parkingSessions", session);
            // Update slot status
            firebaseService.update("parkingSlots", slotId, Map.of("status", "OCCUPIED"));
            ra.addFlashAttribute("success", "Parking session started for " + licensePlate);
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/staff/sessions";
    }

    @GetMapping("/end/{id}")
    public String endForm(@PathVariable String id, Model model) {
        try {
            Map<String, Object> session = firebaseService.getById("parkingSessions", id);
            model.addAttribute("session", session);
            // Calculate fee
            if (session != null) {
                Date entryTime = null;
                Object entry = session.get("entryTime");
                if (entry instanceof com.google.cloud.Timestamp) entryTime = ((com.google.cloud.Timestamp) entry).toDate();
                else if (entry instanceof Date) entryTime = (Date) entry;
                if (entryTime != null) {
                    long hours = Math.max(1, (new Date().getTime() - entryTime.getTime()) / (1000 * 60 * 60));
                    List<Map<String, Object>> policies = firebaseService.getAll("pricingPolicies");
                    double fee = hours * 5000; // default fee
                    for (Map<String, Object> p : policies) {
                        if (session.getOrDefault("vehicleTypeName", "").equals(p.get("vehicleTypeName"))) {
                            double base = ((Number) p.getOrDefault("basePrice", 0)).doubleValue();
                            double perHour = ((Number) p.getOrDefault("pricePerHour", 0)).doubleValue();
                            fee = base + (hours * perHour);
                            break;
                        }
                    }
                    model.addAttribute("estimatedFee", fee);
                    model.addAttribute("parkingHours", hours);
                }
            }
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "sessions/end";
    }

    @PostMapping("/end/{id}")
    public String endSession(@PathVariable String id, @RequestParam String exitGate,
                            @RequestParam double finalFee, RedirectAttributes ra) {
        try {
            Map<String, Object> session = firebaseService.getById("parkingSessions", id);
            Map<String, Object> updates = new HashMap<>();
            updates.put("exitTime", new Date());
            updates.put("exitGate", exitGate);
            updates.put("finalFee", finalFee);
            updates.put("status", "COMPLETED");
            firebaseService.update("parkingSessions", id, updates);
            // Free the slot
            if (session != null && session.get("slotId") != null) {
                firebaseService.update("parkingSlots", (String) session.get("slotId"), Map.of("status", "AVAILABLE"));
            }
            ra.addFlashAttribute("success", "Session ended. Fee: " + finalFee + " VND");
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/staff/sessions";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable String id, Model model) {
        try { model.addAttribute("session", firebaseService.getById("parkingSessions", id)); }
        catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "sessions/view";
    }
}
