package com.nova.nova_siem_backend.siem.ingestion.controller;

import com.nova.nova_siem_backend.siem.ingestion.service.ExcelIngestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ingest")
public class ExcelIngestionController {

    private final ExcelIngestionService ingestionService;

    public ExcelIngestionController(ExcelIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/excel")
    public ResponseEntity<Map<String, Object>> uploadExcelEvents(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = ingestionService.processExcelFile(file);
        return ResponseEntity.ok(result);
    }
}