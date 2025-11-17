package com.garageid.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('OPERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> me(Authentication auth) {
        User u = userRepository.findByEmail(auth.getName()).orElse(null);
        if (u == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new UserProfile(u.getName(), u.getPhone(), u.getEmail(), u.getRole()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> list() { return userRepository.findAll(); }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> setRole(@PathVariable String id, @RequestParam Role role) {
        User u = userRepository.findById(id).orElse(null);
        if (u == null) return ResponseEntity.notFound().build();
        u.setRole(role);
        userRepository.save(u);
        return ResponseEntity.ok().build();
    }

    public static class UserProfile {
        private final String name;
        private final String phone;
        private final String email;
        private final Role role;

        public UserProfile(String name, String phone, String email, Role role) {
            this.name = name; this.phone = phone; this.email = email; this.role = role;
        }
        public String getName() { return name; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public Role getRole() { return role; }
    }
}