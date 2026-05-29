package com.parking.controller;

import com.parking.model.User;
import com.parking.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        if (error != null) model.addAttribute("error", "Invalid email or password!");
        if (logout != null) model.addAttribute("message", "You have been logged out successfully.");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String fullName,
                          @RequestParam String email,
                          @RequestParam String phoneNumber,
                          @RequestParam String password,
                          @RequestParam(defaultValue = "DRIVER") String role,
                          Model model) {
        String result = userDetailsService.registerUser(fullName, email, phoneNumber, password, role);
        if ("SUCCESS".equals(result)) {
            model.addAttribute("message", "Registration successful! Please login.");
            return "login";
        } else {
            model.addAttribute("error", result);
            return "register";
        }
    }
}
