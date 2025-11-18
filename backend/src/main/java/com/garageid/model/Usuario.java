package com.garageid.model;

import com.garageid.security.Role;

import java.util.List;

public class Usuario extends UsuarioBase {
  public Usuario(String nombre, String email, String passwordHash) {
    super(nombre, email, passwordHash, Role.USER);
  }

  @Override
  public List<String> getPermisos() {
    return List.of("vehiculos.crear", "reservas.crear", "reservas.ver");
  }
}