package com.parking.controller;

import com.parking.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/manager/buildings")
public class BuildingController {

    @Autowired
    private FirebaseService firebaseService;

    @GetMapping
    public String list(Model model) {
        try {
            model.addAttribute("buildings", firebaseService.getAll("buildings"));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "buildings/list";
    }

    @GetMapping("/create")
    public String createForm() { return "buildings/form"; }

    @PostMapping("/save")
    public String save(@RequestParam String buildingName,
                       @RequestParam String address,
                       @RequestParam int totalFloors,
                       @RequestParam String operatingStartTime,
                       @RequestParam String operatingEndTime,
                       @RequestParam(required = false) String id,
                       RedirectAttributes redirectAttributes) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("buildingName", buildingName);
            data.put("address", address);
            data.put("totalFloors", totalFloors);
            data.put("operatingStartTime", operatingStartTime);
            data.put("operatingEndTime", operatingEndTime);
            data.put("createdAt", new Date());

            if (id != null && !id.isEmpty()) {
                firebaseService.update("buildings", id, data);
                redirectAttributes.addFlashAttribute("success", "Building updated successfully!");
            } else {
                firebaseService.save("buildings", data);
                redirectAttributes.addFlashAttribute("success", "Building created successfully!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/manager/buildings";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        try {
            model.addAttribute("building", firebaseService.getById("buildings", id));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "buildings/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            firebaseService.delete("buildings", id);
            redirectAttributes.addFlashAttribute("success", "Building deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/manager/buildings";
    }
}
