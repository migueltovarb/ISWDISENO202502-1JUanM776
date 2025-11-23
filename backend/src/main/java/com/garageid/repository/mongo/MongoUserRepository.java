package com.garageid.repository.mongo;

import com.garageid.model.Administrador;
import com.garageid.model.Operador;
import com.garageid.model.Usuario;
import com.garageid.model.UsuarioBase;
import com.garageid.repository.UserRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MongoUserRepository implements UserRepository {
  private final MongoCollection<Document> col;

  public MongoUserRepository(MongoTemplate template) {
    this.col = template.getDb().getCollection("users");
  }

  @Override
  public UsuarioBase save(UsuarioBase user) {
    Document d = new Document("_id", user.getId())
        .append("nombre", user.getNombre())
        .append("email", user.getEmail())
        .append("passwordHash", user.getPasswordHash())
        .append("role", user.getRole().name());
    col.replaceOne(Filters.eq("_id", user.getId()), d, new ReplaceOptions().upsert(true));
    return user;
  }

  @Override
  public Optional<UsuarioBase> findById(String id) {
    Document d = col.find(Filters.eq("_id", id)).first();
    return Optional.ofNullable(map(d));
  }

  @Override
  public Optional<UsuarioBase> findByEmail(String email) {
    Document d = col.find(Filters.eq("email", email)).first();
    return Optional.ofNullable(map(d));
  }

  @Override
  public List<UsuarioBase> findAll() {
    List<UsuarioBase> out = new ArrayList<>();
    for (Document d : col.find()) out.add(map(d));
    return out;
  }

  private UsuarioBase map(Document d) {
    if (d == null) return null;
    String id = d.getString("_id");
    String nombre = d.getString("nombre");
    String email = d.getString("email");
    String passwordHash = d.getString("passwordHash");
    String role = d.getString("role");
    if ("ADMIN".equalsIgnoreCase(role)) return new Administrador(id, nombre, email, passwordHash);
    if ("OPERATOR".equalsIgnoreCase(role)) return new Operador(id, nombre, email, passwordHash);
    return new Usuario(id, nombre, email, passwordHash);
  }
}
