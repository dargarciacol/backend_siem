package com.nova.nova_siem_backend.siem.ingestion.service;

import com.nova.nova_siem_backend.siem.common.exception.BusinessException;
import com.nova.nova_siem_backend.siem.events.model.SecurityEvent;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ExcelParserService {

    private final EventNormalizationService normalizationService;

    public ExcelParserService(EventNormalizationService normalizationService) {
        this.normalizationService = normalizationService;
    }

    public List<SecurityEvent> parseEvents(MultipartFile file) {
        List<SecurityEvent> events = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();

                String sourceIp = getCellValue(currentRow.getCell(0));
                String userIdentity = getCellValue(currentRow.getCell(1));
                String eventType = getCellValue(currentRow.getCell(2));
                String action = getCellValue(currentRow.getCell(3));
                String severity = getCellValue(currentRow.getCell(4));

                Map<String, Object> rawData = new HashMap<>();
                rawData.put("ingestion_source", file.getOriginalFilename());
                rawData.put("row_number", currentRow.getRowNum());

                SecurityEvent event = normalizationService.normalizeEvent(
                        sourceIp, userIdentity, eventType, action, severity, rawData);

                events.add(event);
            }
        } catch (Exception e) {
            throw new BusinessException("Failed to parse Excel file: " + e.getMessage());
        }

        return events;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}