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
@RequestMapping("/staff/incidents")
public class IncidentController {
    @Autowired private FirebaseService firebaseService;
    @Autowired private CustomUserDetailsService userService;

    @GetMapping
    public String list(@RequestParam(required = false) String type, Model model) {
        try {
            List<Map<String, Object>> incidents;
            if (type != null && !type.isEmpty()) {
                incidents = firebaseService.getByField("incidentReports", "incidentType", type);
            } else {
                incidents = firebaseService.getAll("incidentReports");
            }
            Collections.reverse(incidents);
            model.addAttribute("incidents", incidents);
            model.addAttribute("filterType", type);
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "incidents/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        try {
            model.addAttribute("sessions", firebaseService.getByField("parkingSessions", "status", "PARKING"));
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "incidents/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam(required = false) String sessionId,
                       @RequestParam String incidentType, @RequestParam String description,
                       Authentication auth, RedirectAttributes ra) {
        try {
            User user = userService.getUserByEmail(auth.getName());
            Map<String, Object> data = new HashMap<>();
            data.put("sessionId", sessionId != null ? sessionId : "");
            data.put("reportedBy", user != null ? user.getId() : "");
            data.put("reportedByName", user != null ? user.getFullName() : "");
            data.put("incidentType", incidentType); data.put("description", description);
            data.put("status", "OPEN"); data.put("createdAt", new Date());
            firebaseService.save("incidentReports", data);
            // Update session status if needed
            if (sessionId != null && !sessionId.isEmpty()) {
                if ("LOST_TICKET".equals(incidentType)) {
                    firebaseService.update("parkingSessions", sessionId, Map.of("status", "LOST_TICKET"));
                }
            }
            ra.addFlashAttribute("success", "Incident reported!");
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/staff/incidents";
    }

    @PostMapping("/update-status/{id}")
    public String updateStatus(@PathVariable String id, @RequestParam String status, RedirectAttributes ra) {
        try {
            firebaseService.update("incidentReports", id, Map.of("status", status));
            ra.addFlashAttribute("success", "Status updated to " + status);
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/staff/incidents";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        try { firebaseService.delete("incidentReports", id); ra.addFlashAttribute("success", "Deleted!"); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/staff/incidents";
    }
}
