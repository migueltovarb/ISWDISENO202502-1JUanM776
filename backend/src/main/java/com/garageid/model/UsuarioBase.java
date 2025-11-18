package com.garageid.model;

import com.garageid.security.Role;

import java.util.List;
import java.util.UUID;

public abstract class UsuarioBase {
  private final String id;
  private String nombre;
  private String email;
  private String passwordHash;
  private Role role;

  public UsuarioBase(String nombre, String email, String passwordHash, Role role) {
    this.id = UUID.randomUUID().toString();
    this.nombre = nombre;
    this.email = email;
    this.passwordHash = passwordHash;
    this.role = role;
  }

  public String getId() { return id; }
  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }
  public String getEmail() { return email; }
  public void setEmail(String email) { this.email = email; }
  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
  public Role getRole() { return role; }
  public void setRole(Role role) { this.role = role; }

  public abstract List<String> getPermisos();
}