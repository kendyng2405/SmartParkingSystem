package com.parking.controller;

import com.parking.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Controller
@RequestMapping("/manager/pricing")
public class PricingController {
    @Autowired private FirebaseService firebaseService;

    @GetMapping
    public String list(Model model) {
        try {
            model.addAttribute("policies", firebaseService.getAll("pricingPolicies"));
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "pricing/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        try { model.addAttribute("vehicleTypes", firebaseService.getAll("vehicleTypes")); }
        catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "pricing/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam String vehicleTypeId, @RequestParam String vehicleTypeName,
                       @RequestParam String policyName, @RequestParam double basePrice,
                       @RequestParam double pricePerHour, @RequestParam double maxDailyRate,
                       @RequestParam double lostTicketFee, @RequestParam double overtimeFeePerHour,
                       @RequestParam String effectiveFrom, @RequestParam(required = false) String effectiveTo,
                       @RequestParam(required = false) String id, RedirectAttributes ra) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("vehicleTypeId", vehicleTypeId); data.put("vehicleTypeName", vehicleTypeName);
            data.put("policyName", policyName); data.put("basePrice", basePrice);
            data.put("pricePerHour", pricePerHour); data.put("maxDailyRate", maxDailyRate);
            data.put("lostTicketFee", lostTicketFee); data.put("overtimeFeePerHour", overtimeFeePerHour);
            data.put("effectiveFrom", effectiveFrom); data.put("effectiveTo", effectiveTo != null ? effectiveTo : "");
            if (id != null && !id.isEmpty()) { firebaseService.update("pricingPolicies", id, data); ra.addFlashAttribute("success", "Updated!"); }
            else { firebaseService.save("pricingPolicies", data); ra.addFlashAttribute("success", "Created!"); }
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/manager/pricing";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        try {
            model.addAttribute("policy", firebaseService.getById("pricingPolicies", id));
            model.addAttribute("vehicleTypes", firebaseService.getAll("vehicleTypes"));
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "pricing/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        try { firebaseService.delete("pricingPolicies", id); ra.addFlashAttribute("success", "Deleted!"); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/manager/pricing";
    }
}
