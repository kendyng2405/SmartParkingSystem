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
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/staff/reservations")
public class ReservationController {
    @Autowired private FirebaseService firebaseService;
    @Autowired private CustomUserDetailsService userService;

    @GetMapping
    public String list(Model model) {
        try {
            List<Map<String, Object>> reservations = firebaseService.getAll("reservations");
            Collections.reverse(reservations);
            model.addAttribute("reservations", reservations);
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "reservations/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        try {
            model.addAttribute("vehicleTypes", firebaseService.getAll("vehicleTypes"));
            model.addAttribute("availableSlots", firebaseService.getByField("parkingSlots", "status", "AVAILABLE"));
            model.addAttribute("users", firebaseService.getAll("users"));
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "reservations/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam String userId, @RequestParam String userName,
                       @RequestParam String vehicleTypeId, @RequestParam String vehicleTypeName,
                       @RequestParam String slotId, @RequestParam String slotCode,
                       @RequestParam String reservationStart, @RequestParam String reservationEnd,
                       @RequestParam(required = false) String id, RedirectAttributes ra) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId); data.put("userName", userName);
            data.put("vehicleTypeId", vehicleTypeId); data.put("vehicleTypeName", vehicleTypeName);
            data.put("slotId", slotId); data.put("slotCode", slotCode);
            data.put("reservationStart", sdf.parse(reservationStart));
            data.put("reservationEnd", sdf.parse(reservationEnd));
            data.put("status", "CONFIRMED"); data.put("createdAt", new Date());

            if (id != null && !id.isEmpty()) {
                firebaseService.update("reservations", id, data);
                ra.addFlashAttribute("success", "Reservation updated!");
            } else {
                firebaseService.save("reservations", data);
                firebaseService.update("parkingSlots", slotId, Map.of("status", "RESERVED"));
                ra.addFlashAttribute("success", "Reservation created!");
            }
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/staff/reservations";
    }

    @GetMapping("/cancel/{id}")
    public String cancel(@PathVariable String id, RedirectAttributes ra) {
        try {
            Map<String, Object> reservation = firebaseService.getById("reservations", id);
            firebaseService.update("reservations", id, Map.of("status", "CANCELLED"));
            if (reservation != null && reservation.get("slotId") != null) {
                firebaseService.update("parkingSlots", (String) reservation.get("slotId"), Map.of("status", "AVAILABLE"));
            }
            ra.addFlashAttribute("success", "Reservation cancelled!");
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/staff/reservations";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        try { firebaseService.delete("reservations", id); ra.addFlashAttribute("success", "Deleted!"); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/staff/reservations";
    }
}
