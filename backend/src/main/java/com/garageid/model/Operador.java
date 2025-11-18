package com.garageid.model;

import com.garageid.security.Role;

import java.util.List;

public class Operador extends UsuarioBase {
  public Operador(String nombre, String email, String passwordHash) {
    super(nombre, email, passwordHash, Role.OPERATOR);
  }

  @Override
  public List<String> getPermisos() {
    return List.of("entradas.registrar", "salidas.registrar");
  }
}