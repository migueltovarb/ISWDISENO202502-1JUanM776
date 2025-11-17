package com.garageid.user;

import com.garageid.security.JwtTokenProvider;
import com.garageid.user.dto.LoginRequest;
import com.garageid.user.dto.PasswordResetRequest;
import com.garageid.user.dto.RegisterRequest;
import com.garageid.user.dto.SetNewPasswordRequest;
import com.garageid.notification.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) return ResponseEntity.badRequest().body("email_in_use");
        User u = new User();
        u.setName(req.getName());
        u.setPhone(req.getPhone());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setRole(Role.USER);
        userRepository.save(u);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest req) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        User u = userRepository.findByEmail(req.getEmail()).orElseThrow();
        String token = jwtTokenProvider.generateToken(u.getEmail(), u.getRole().name());
        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("role", u.getRole().name());
        return ResponseEntity.ok(body);
    }

    @PostMapping("/password/reset/request")
    public ResponseEntity<?> requestReset(@Valid @RequestBody PasswordResetRequest req) {
        User u = userRepository.findByEmail(req.getEmail()).orElse(null);
        if (u == null) return ResponseEntity.ok().build();
        String token = UUID.randomUUID().toString();
        u.setPasswordResetToken(token);
        u.setPasswordResetExpiresAt(Instant.now().plusSeconds(3600));
        userRepository.save(u);
        emailService.sendPasswordReset(req.getEmail(), token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset/confirm")
    public ResponseEntity<?> confirmReset(@Valid @RequestBody SetNewPasswordRequest req) {
        User u = userRepository.findAll().stream().filter(x -> req.getToken().equals(x.getPasswordResetToken())).findFirst().orElse(null);
        if (u == null || u.getPasswordResetExpiresAt() == null || u.getPasswordResetExpiresAt().isBefore(Instant.now())) return ResponseEntity.badRequest().body("invalid_token");
        u.setPassword(passwordEncoder.encode(req.getNewPassword()));
        u.setPasswordResetToken(null);
        u.setPasswordResetExpiresAt(null);
        userRepository.save(u);
        return ResponseEntity.ok().build();
    }
}