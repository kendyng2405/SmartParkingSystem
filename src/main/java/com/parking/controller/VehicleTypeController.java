package com.parking.controller;

import com.parking.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/manager/vehicle-types")
public class VehicleTypeController {

    @Autowired private FirebaseService firebaseService;

    @GetMapping
    public String list(Model model) {
        try { model.addAttribute("vehicleTypes", firebaseService.getAll("vehicleTypes")); }
        catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "vehicle-types/list";
    }

    @GetMapping("/create")
    public String createForm() { return "vehicle-types/form"; }

    @PostMapping("/save")
    public String save(@RequestParam String typeName, @RequestParam String description,
                       @RequestParam(required = false) String id, RedirectAttributes ra) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("typeName", typeName);
            data.put("description", description);
            if (id != null && !id.isEmpty()) { firebaseService.update("vehicleTypes", id, data); ra.addFlashAttribute("success", "Updated!"); }
            else { firebaseService.save("vehicleTypes", data); ra.addFlashAttribute("success", "Created!"); }
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/manager/vehicle-types";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        try { model.addAttribute("vehicleType", firebaseService.getById("vehicleTypes", id)); }
        catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "vehicle-types/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        try { firebaseService.delete("vehicleTypes", id); ra.addFlashAttribute("success", "Deleted!"); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/manager/vehicle-types";
    }
}
