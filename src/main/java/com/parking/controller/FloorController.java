package com.parking.controller;

import com.parking.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/manager/floors")
public class FloorController {

    @Autowired private FirebaseService firebaseService;

    @GetMapping
    public String list(Model model) {
        try {
            model.addAttribute("floors", firebaseService.getAll("floors"));
            model.addAttribute("buildings", firebaseService.getAll("buildings"));
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "floors/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        try { model.addAttribute("buildings", firebaseService.getAll("buildings")); }
        catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "floors/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam String buildingId, @RequestParam String buildingName,
                       @RequestParam int floorNumber, @RequestParam String floorName,
                       @RequestParam(required = false) String id, RedirectAttributes ra) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("buildingId", buildingId); data.put("buildingName", buildingName);
            data.put("floorNumber", floorNumber); data.put("floorName", floorName);
            if (id != null && !id.isEmpty()) { firebaseService.update("floors", id, data); ra.addFlashAttribute("success", "Updated!"); }
            else { firebaseService.save("floors", data); ra.addFlashAttribute("success", "Created!"); }
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/manager/floors";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        try {
            model.addAttribute("floor", firebaseService.getById("floors", id));
            model.addAttribute("buildings", firebaseService.getAll("buildings"));
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "floors/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        try { firebaseService.delete("floors", id); ra.addFlashAttribute("success", "Deleted!"); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/manager/floors";
    }
}
