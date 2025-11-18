package com.garageid.repository.inmemory;

import com.garageid.model.UsuarioBase;
import com.garageid.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {
  private final Map<String, UsuarioBase> users = new ConcurrentHashMap<>();

  @Override
  public UsuarioBase save(UsuarioBase user) {
    users.put(user.getId(), user);
    return user;
  }

  @Override
  public Optional<UsuarioBase> findById(String id) { return Optional.ofNullable(users.get(id)); }

  @Override
  public Optional<UsuarioBase> findByEmail(String email) {
    return users.values().stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst();
  }

  @Override
  public List<UsuarioBase> findAll() { return new ArrayList<>(users.values()); }
}