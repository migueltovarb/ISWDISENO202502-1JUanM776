package com.garageid.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration-ms}")
  private long expirationMs;

  private Key getSigningKey() {
    byte[] keyBytes = secret.getBytes();
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(String userId, String email, Role role) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + expirationMs);
    return Jwts.builder()
        .setSubject(userId)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .addClaims(Map.of("email", email, "role", role.name()))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractUserId(String token) {
    return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
        .parseClaimsJws(token).getBody().getSubject();
  }

  public String extractRole(String token) {
    Object role = Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
        .parseClaimsJws(token).getBody().get("role");
    return role == null ? null : role.toString();
  }

  public boolean isValid(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }
}