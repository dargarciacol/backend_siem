
package com.nova.nova_siem_backend.siem.ingestion.controller;

import com.nova.nova_siem_backend.siem.common.response.ApiResponse;
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
    public ResponseEntity<ApiResponse> uploadExcelEvents(
            @RequestParam("file") MultipartFile file) {

        // 1. Validación temprana: archivo vacío
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.error(
                            "El archivo recibido está vacío.",
                            "FILE_EMPTY"
                    )
            );
        }

        // 2. Validación temprana: extensión del archivo
        String filename = file.getOriginalFilename();

        if (filename == null ||
                (!filename.toLowerCase().endsWith(".xlsx") &&
                        !filename.toLowerCase().endsWith(".xls"))) {

            return ResponseEntity.badRequest().body(
                    ApiResponse.error("Formato inválido. Se requiere un archivo Excel (.xlsx o .xls).",
                            "INVALID_FORMAT")
            );
        }

        // 3. Procesamiento real
        Map<String, Object> result = ingestionService.processExcelFile(file);

        // 4. Respuesta estandarizada
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Archivo Excel procesado correctamente.",
                        result
                )
        );
    }
}
