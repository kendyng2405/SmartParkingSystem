package com.parking.controller;

import com.parking.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Controller
@RequestMapping("/manager/zones")
public class ZoneController {
    @Autowired private FirebaseService firebaseService;

    @GetMapping
    public String list(Model model) {
        try {
            model.addAttribute("zones", firebaseService.getAll("zones"));
            model.addAttribute("floors", firebaseService.getAll("floors"));
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "zones/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        try { model.addAttribute("floors", firebaseService.getAll("floors")); }
        catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "zones/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam String floorId, @RequestParam String floorName,
                       @RequestParam String zoneName, @RequestParam String description,
                       @RequestParam(required = false) String id, RedirectAttributes ra) {
        try {
            Map<String, Object> data = Map.of("floorId", floorId, "floorName", floorName, "zoneName", zoneName, "description", description);
            if (id != null && !id.isEmpty()) { firebaseService.update("zones", id, new HashMap<>(data)); ra.addFlashAttribute("success", "Updated!"); }
            else { firebaseService.save("zones", new HashMap<>(data)); ra.addFlashAttribute("success", "Created!"); }
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/manager/zones";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        try { model.addAttribute("zone", firebaseService.getById("zones", id)); model.addAttribute("floors", firebaseService.getAll("floors")); }
        catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "zones/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        try { firebaseService.delete("zones", id); ra.addFlashAttribute("success", "Deleted!"); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/manager/zones";
    }
}
