package com.garageid.service;

import com.garageid.dto.*;
import com.garageid.model.*;
import com.garageid.repository.UserRepository;
import com.garageid.security.JwtUtil;
import com.garageid.security.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
  private final UserRepository users;
  private final PasswordEncoder encoder;
  private final JwtUtil jwt;

  public AuthService(UserRepository users, PasswordEncoder encoder, JwtUtil jwt) {
    this.users = users; this.encoder = encoder; this.jwt = jwt;
  }

  public AuthResponse register(RegisterRequest req) {
    Role role = Role.valueOf(req.getRole().toUpperCase());
    String hash = encoder.encode(req.getPassword());
    UsuarioBase u = switch (role) {
      case USER -> new Usuario(req.getNombre(), req.getEmail(), hash);
      case OPERATOR -> new Operador(req.getNombre(), req.getEmail(), hash);
      case ADMIN -> new Administrador(req.getNombre(), req.getEmail(), hash);
    };
    users.save(u);
    String token = jwt.generateToken(u.getId(), u.getEmail(), role);
    return new AuthResponse(token, role.name());
  }

  public AuthResponse login(LoginRequest req) {
    Optional<UsuarioBase> opt = users.findByEmail(req.getEmail());
    if (opt.isEmpty()) throw new RuntimeException("Credenciales inválidas");
    UsuarioBase u = opt.get();
    if (!encoder.matches(req.getPassword(), u.getPasswordHash())) throw new RuntimeException("Credenciales inválidas");
    String token = jwt.generateToken(u.getId(), u.getEmail(), u.getRole());
    return new AuthResponse(token, u.getRole().name());
  }

  public UserDTO me(String userId) {
    UsuarioBase u = users.findById(userId).orElseThrow();
    return new UserDTO(u.getId(), u.getNombre(), u.getEmail(), u.getRole().name());
  }
}