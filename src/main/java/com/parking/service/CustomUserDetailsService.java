package com.parking.service;

import com.parking.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        firebaseService.initializeDefaultData();
        // Create default admin user
        try {
            List<Map<String, Object>> admins = firebaseService.getByField("users", "email", "admin@parking.com");
            if (admins.isEmpty()) {
                Map<String, Object> admin = new HashMap<>();
                admin.put("fullName", "System Administrator");
                admin.put("email", "admin@parking.com");
                admin.put("phoneNumber", "0123456789");
                admin.put("passwordHash", passwordEncoder.encode("admin123"));
                admin.put("roleName", "ADMIN");
                admin.put("isActive", true);
                admin.put("createdAt", new Date());
                firebaseService.save("users", admin);

                // Create demo manager
                Map<String, Object> manager = new HashMap<>();
                manager.put("fullName", "Parking Manager");
                manager.put("email", "manager@parking.com");
                manager.put("phoneNumber", "0123456788");
                manager.put("passwordHash", passwordEncoder.encode("manager123"));
                manager.put("roleName", "MANAGER");
                manager.put("isActive", true);
                manager.put("createdAt", new Date());
                firebaseService.save("users", manager);

                // Create demo staff
                Map<String, Object> staff = new HashMap<>();
                staff.put("fullName", "Parking Staff");
                staff.put("email", "staff@parking.com");
                staff.put("phoneNumber", "0123456787");
                staff.put("passwordHash", passwordEncoder.encode("staff123"));
                staff.put("roleName", "STAFF");
                staff.put("isActive", true);
                staff.put("createdAt", new Date());
                firebaseService.save("users", staff);

                // Create demo driver
                Map<String, Object> driver = new HashMap<>();
                driver.put("fullName", "Demo Driver");
                driver.put("email", "driver@parking.com");
                driver.put("phoneNumber", "0123456786");
                driver.put("passwordHash", passwordEncoder.encode("driver123"));
                driver.put("roleName", "DRIVER");
                driver.put("isActive", true);
                driver.put("createdAt", new Date());
                firebaseService.save("users", driver);
            }
        } catch (Exception e) {
            System.err.println("Error creating default users: " + e.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            List<Map<String, Object>> users = firebaseService.getByField("users", "email", email);
            if (users.isEmpty()) {
                throw new UsernameNotFoundException("User not found: " + email);
            }
            Map<String, Object> userData = users.get(0);
            String roleName = (String) userData.getOrDefault("roleName", "DRIVER");
            boolean isActive = (Boolean) userData.getOrDefault("isActive", true);

            return org.springframework.security.core.userdetails.User.builder()
                    .username(email)
                    .password((String) userData.get("passwordHash"))
                    .disabled(!isActive)
                    .authorities(new SimpleGrantedAuthority("ROLE_" + roleName))
                    .build();
        } catch (UsernameNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error loading user: " + e.getMessage());
        }
    }

    public User getUserByEmail(String email) {
        try {
            List<Map<String, Object>> users = firebaseService.getByField("users", "email", email);
            if (!users.isEmpty()) {
                return User.fromMap((String) users.get(0).get("id"), users.get(0));
            }
        } catch (Exception e) {
            System.err.println("Error getting user: " + e.getMessage());
        }
        return null;
    }

    public String registerUser(String fullName, String email, String phone, String password, String roleName) {
        try {
            List<Map<String, Object>> existing = firebaseService.getByField("users", "email", email);
            if (!existing.isEmpty()) return "Email already exists";

            Map<String, Object> user = new HashMap<>();
            user.put("fullName", fullName);
            user.put("email", email);
            user.put("phoneNumber", phone);
            user.put("passwordHash", passwordEncoder.encode(password));
            user.put("roleName", roleName);
            user.put("isActive", true);
            user.put("createdAt", new Date());
            firebaseService.save("users", user);
            return "SUCCESS";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
