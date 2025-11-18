package com.garageid.controller;

import com.garageid.service.OperationsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/ops")
@CrossOrigin(origins = "*")
public class OperatorController {
  private final OperationsService ops;
  public OperatorController(OperationsService ops) { this.ops = ops; }

  @PostMapping("/entry")
  public String entry(@RequestBody Map<String, String> body) { return ops.registerEntry(body.get("plate")); }

  @PostMapping("/exit")
  public String exit(@RequestBody Map<String, String> body) { return ops.registerExit(body.get("plate")); }

  @GetMapping("/history")
  public List<Map<String, Object>> history(){
    var raw = ops.listHistory();
    return raw.stream().map(m -> {
      Map<String, Object> out = new java.util.LinkedHashMap<>();
      out.put("id", m.get("id"));
      out.put("type", m.get("type"));
      out.put("plate", m.get("plate"));
      out.put("createdAt", String.valueOf(m.get("at")));
      return out;
    }).toList();
  }

  @DeleteMapping("/history")
  public Map<String, String> clear(){ ops.clearHistory(); return Map.of("status", "CLEARED"); }
}