package com.garageid.controller;

import com.garageid.dto.*;
import com.garageid.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {
  private final AuthService auth;
  public AuthController(AuthService auth) { this.auth = auth; }

  @PostMapping("/register")
  public AuthResponse register(@RequestBody RegisterRequest req) { return auth.register(req); }

  @PostMapping("/login")
  public AuthResponse login(@RequestBody LoginRequest req) { return auth.login(req); }
}