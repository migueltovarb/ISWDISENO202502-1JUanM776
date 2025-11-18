package com.garageid.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OperationsService {
  private final List<Map<String, Object>> history = Collections.synchronizedList(new ArrayList<>());

  public String registerEntry(String plate) {
    addRecord("ENTRY", plate);
    return "Entrada registrada para placa " + plate;
  }

  public String registerExit(String plate) {
    addRecord("EXIT", plate);
    return "Salida registrada para placa " + plate;
  }

  public List<Map<String, Object>> listHistory(){
    return new ArrayList<>(history);
  }

  public void clearHistory(){
    history.clear();
  }

  private void addRecord(String type, String plate){
    Map<String, Object> m = new LinkedHashMap<>();
    m.put("id", UUID.randomUUID().toString());
    m.put("type", type);
    m.put("plate", plate);
    m.put("at", LocalDateTime.now());
    history.add(m);
  }
}