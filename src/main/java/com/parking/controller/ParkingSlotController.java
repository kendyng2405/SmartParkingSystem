package com.parking.controller;

import com.parking.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Controller
@RequestMapping("/manager/slots")
public class ParkingSlotController {
    @Autowired private FirebaseService firebaseService;

    @GetMapping
    public String list(@RequestParam(required = false) String status, Model model) {
        try {
            List<Map<String, Object>> slots;
            if (status != null && !status.isEmpty()) {
                slots = firebaseService.getByField("parkingSlots", "status", status);
            } else {
                slots = firebaseService.getAll("parkingSlots");
            }
            model.addAttribute("slots", slots);
            model.addAttribute("filterStatus", status);
            model.addAttribute("zones", firebaseService.getAll("zones"));
            model.addAttribute("vehicleTypes", firebaseService.getAll("vehicleTypes"));
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "slots/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        try {
            model.addAttribute("zones", firebaseService.getAll("zones"));
            model.addAttribute("vehicleTypes", firebaseService.getAll("vehicleTypes"));
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "slots/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam String zoneId, @RequestParam String zoneName,
                       @RequestParam String slotCode, @RequestParam String vehicleTypeId,
                       @RequestParam String vehicleTypeName,
                       @RequestParam(defaultValue = "AVAILABLE") String status,
                       @RequestParam(required = false) String id, RedirectAttributes ra) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("zoneId", zoneId); data.put("zoneName", zoneName);
            data.put("slotCode", slotCode); data.put("vehicleTypeId", vehicleTypeId);
            data.put("vehicleTypeName", vehicleTypeName); data.put("status", status);
            data.put("isActive", true);
            if (id != null && !id.isEmpty()) { firebaseService.update("parkingSlots", id, data); ra.addFlashAttribute("success", "Updated!"); }
            else { firebaseService.save("parkingSlots", data); ra.addFlashAttribute("success", "Created!"); }
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/manager/slots";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        try {
            model.addAttribute("slot", firebaseService.getById("parkingSlots", id));
            model.addAttribute("zones", firebaseService.getAll("zones"));
            model.addAttribute("vehicleTypes", firebaseService.getAll("vehicleTypes"));
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "slots/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        try { firebaseService.delete("parkingSlots", id); ra.addFlashAttribute("success", "Deleted!"); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/manager/slots";
    }

    @PostMapping("/update-status/{id}")
    public String updateStatus(@PathVariable String id, @RequestParam String status, RedirectAttributes ra) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("status", status);
            firebaseService.update("parkingSlots", id, data);
            ra.addFlashAttribute("success", "Slot status updated to " + status);
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/manager/slots";
    }

    @GetMapping("/lock/{id}")
    public String lock(@PathVariable String id, RedirectAttributes ra) {
        try { firebaseService.update("parkingSlots", id, Map.of("status", "LOCKED")); ra.addFlashAttribute("success", "Slot locked!"); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/manager/slots";
    }

    @GetMapping("/unlock/{id}")
    public String unlock(@PathVariable String id, RedirectAttributes ra) {
        try { firebaseService.update("parkingSlots", id, Map.of("status", "AVAILABLE")); ra.addFlashAttribute("success", "Slot unlocked!"); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/manager/slots";
    }
}
