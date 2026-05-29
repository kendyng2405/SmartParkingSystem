package com.parking.controller;

import com.parking.service.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Controller
@RequestMapping("/admin/users")
public class UserController {
    @Autowired private FirebaseService firebaseService;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping
    public String list(Model model) {
        try { model.addAttribute("users", firebaseService.getAll("users")); }
        catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "users/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("roles", List.of("ADMIN", "MANAGER", "STAFF", "DRIVER"));
        return "users/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam String fullName, @RequestParam String email,
                       @RequestParam String phoneNumber, @RequestParam String roleName,
                       @RequestParam(required = false) String password,
                       @RequestParam(required = false) String id, RedirectAttributes ra) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("fullName", fullName); data.put("email", email);
            data.put("phoneNumber", phoneNumber); data.put("roleName", roleName);
            data.put("isActive", true);
            if (id != null && !id.isEmpty()) {
                if (password != null && !password.isEmpty()) data.put("passwordHash", passwordEncoder.encode(password));
                firebaseService.update("users", id, data);
                ra.addFlashAttribute("success", "User updated!");
            } else {
                data.put("passwordHash", passwordEncoder.encode(password != null ? password : "password123"));
                data.put("createdAt", new Date());
                firebaseService.save("users", data);
                ra.addFlashAttribute("success", "User created!");
            }
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        try {
            model.addAttribute("user", firebaseService.getById("users", id));
            model.addAttribute("roles", List.of("ADMIN", "MANAGER", "STAFF", "DRIVER"));
        } catch (Exception e) { model.addAttribute("error", e.getMessage()); }
        return "users/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id, RedirectAttributes ra) {
        try { firebaseService.delete("users", id); ra.addFlashAttribute("success", "User deleted!"); }
        catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/admin/users";
    }

    @PostMapping("/toggle-active/{id}")
    public String toggleActive(@PathVariable String id, @RequestParam boolean isActive, RedirectAttributes ra) {
        try {
            firebaseService.update("users", id, Map.of("isActive", !isActive));
            ra.addFlashAttribute("success", "User status updated!");
        } catch (Exception e) { ra.addFlashAttribute("error", e.getMessage()); }
        return "redirect:/admin/users";
    }
}
