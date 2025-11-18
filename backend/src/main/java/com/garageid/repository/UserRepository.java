package com.garageid.repository;

import com.garageid.model.UsuarioBase;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
  UsuarioBase save(UsuarioBase user);
  Optional<UsuarioBase> findById(String id);
  Optional<UsuarioBase> findByEmail(String email);
  List<UsuarioBase> findAll();
}