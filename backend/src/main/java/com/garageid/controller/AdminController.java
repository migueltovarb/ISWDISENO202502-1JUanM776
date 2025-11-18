package com.garageid.controller;

import com.garageid.dto.ReportDTO;
import com.garageid.service.ReportService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {
  private final ReportService reports;
  public AdminController(ReportService reports) { this.reports = reports; }

  @GetMapping("/reports/simple")
  public ReportDTO simple(Authentication auth) { return reports.simpleAdminReport(auth.getName()); }
}