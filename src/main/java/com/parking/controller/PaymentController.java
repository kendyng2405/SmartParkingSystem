package com.parking.controller;

import com.parking.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Controller
@RequestMapping("/staff/payments")
public class PaymentController {
    @Autowired private FirebaseService firebaseService;

    @GetMapping
    public String list(Model model) {
        try {
            List<Map<String, Object>> payments = firebaseService.getAll("payments");
            Collections.reverse(payments);
            model.addAttribute("payments", payments);
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "payments/list";
    }

    @GetMapping("/create/{sessionId}")
    public String createForm(@PathVariable String sessionId, Model model) {
        try {
            model.addAttribute("session", firebaseService.getById("parkingSessions", sessionId));
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "payments/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam String sessionId, @RequestParam String licensePlate,
                       @RequestParam double amount, @RequestParam String paymentMethod,
                       RedirectAttributes ra) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("sessionId", sessionId); data.put("licensePlate", licensePlate);
            data.put("amount", amount); data.put("paymentMethod", paymentMethod);
            data.put("paymentStatus", "PAID"); data.put("paidAt", new Date());
            firebaseService.save("payments", data);
            // Update session status
            firebaseService.update("parkingSessions", sessionId, Map.of("status", "COMPLETED", "finalFee", amount));
            ra.addFlashAttribute("success", "Payment recorded: " + amount + " VND");
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/staff/payments";
    }

    @PostMapping("/refund/{id}")
    public String refund(@PathVariable String id, RedirectAttributes ra) {
        try {
            firebaseService.update("payments", id, Map.of("paymentStatus", "REFUNDED"));
            ra.addFlashAttribute("success", "Payment refunded!");
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/staff/payments";
    }
}
