package com.garageid.controller;

import com.garageid.dto.UserDTO;
import com.garageid.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {
  private final AuthService authService;
  public UserController(AuthService authService) { this.authService = authService; }

  @GetMapping("/me")
  public UserDTO me(Authentication authentication) {
    String userId = authentication.getName();
    return authService.me(userId);
  }
}