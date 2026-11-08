package com.nova.nova_siem_backend.siem.ingestion.service;

import com.nova.nova_siem_backend.siem.events.model.EventSeverity;
import com.nova.nova_siem_backend.siem.events.model.SecurityEvent;
import com.nova.nova_siem_backend.siem.events.repository.EventRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ExcelIngestionService {

    private final EventRepository eventRepository;

    public ExcelIngestionService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Map<String, Object> processExcelFile(MultipartFile file) {
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
                SecurityEvent event = new SecurityEvent();

                event.setTimestamp(LocalDateTime.now());
                event.setSourceIp(getCellValue(currentRow.getCell(0)));
                event.setUserIdentity(getCellValue(currentRow.getCell(1)));
                event.setEventType(getCellValue(currentRow.getCell(2)));
                event.setAction(getCellValue(currentRow.getCell(3)));

                String severityStr = getCellValue(currentRow.getCell(4));
                event.setSeverity(severityStr.isEmpty() ? EventSeverity.INFO : EventSeverity.valueOf(severityStr.toUpperCase()));

                Map<String, Object> rawData = new HashMap<>();
                rawData.put("ingestion_source", file.getOriginalFilename());
                rawData.put("row_number", currentRow.getRowNum());
                event.setRawData(rawData);

                event.setSynthetic(true);
                events.add(event);
            }

            eventRepository.saveAll(events);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Events successfully ingested and normalized.");
            response.put("ingested_events_count", events.size());

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error processing Excel file: " + e.getMessage(), e);
        }
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
}