package com.garageid.model;

import com.garageid.security.Role;

import java.util.List;

public class Administrador extends UsuarioBase {
  public Administrador(String nombre, String email, String passwordHash) {
    super(nombre, email, passwordHash, Role.ADMIN);
  }

  @Override
  public List<String> getPermisos() {
    return List.of("parqueaderos.gestionar", "reportes.generar", "actividad.ver");
  }

  public String generarReporte() {
    return "Reporte generado por Administrador";
  }
}