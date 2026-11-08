
package com.nova.nova_siem_backend.siem.ingestion.service;

import com.nova.nova_siem_backend.siem.events.model.EventSeverity;
import com.nova.nova_siem_backend.siem.events.model.SecurityEvent;
import com.nova.nova_siem_backend.siem.events.repository.EventRepository;
import com.nova.nova_siem_backend.siem.ingestion.validator.ExcelFileValidator;
import com.nova.nova_siem_backend.siem.ingestion.event.EventsIngestedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ExcelIngestionService {

    private final EventRepository eventRepository;
    private final ExcelFileValidator fileValidator;
    private final ApplicationEventPublisher eventPublisher;

    public ExcelIngestionService(
            EventRepository eventRepository,
            ExcelFileValidator fileValidator,
            ApplicationEventPublisher eventPublisher) {

        this.eventRepository = eventRepository;
        this.fileValidator = fileValidator;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Map<String, Object> processExcelFile(MultipartFile file) {

        fileValidator.validate(file);

        List<SecurityEvent> events = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            if (workbook.getNumberOfSheets() == 0) {
                throw new IllegalArgumentException("The Excel file has no sheets.");
            }

            Sheet sheet = workbook.getSheetAt(0);

            if (sheet.getPhysicalNumberOfRows() < 2) {
                throw new IllegalArgumentException("The Excel file contains no data rows.");
            }

            DataFormatter formatter = new DataFormatter();
            FormulaEvaluator evaluator =
                    workbook.getCreationHelper().createFormulaEvaluator();

            Row headerRow = sheet.getRow(0);

            Map<String, Integer> columns = new HashMap<>();

            for (Cell cell : headerRow) {
                String header = formatter.formatCellValue(cell, evaluator)
                        .trim()
                        .toLowerCase(Locale.ROOT);

                columns.put(header, cell.getColumnIndex());
            }

            String[] requiredHeaders = {
                    "user",
                    "source_ip",
                    "destination",
                    "log_source",
                    "event_description",
                    "process_commandline",
                    "result",
                    "severity"
            };

            for (String header : requiredHeaders) {
                if (!columns.containsKey(header)) {
                    throw new IllegalArgumentException(
                            "Missing required Excel column: " + header
                    );
                }
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String user = getValue(row, columns, "user", formatter, evaluator);
                String sourceIp = getValue(row, columns, "source_ip", formatter, evaluator);
                String destination = getValue(row, columns, "destination", formatter, evaluator);
                String logSource = getValue(row, columns, "log_source", formatter, evaluator);
                String description = getValue(row, columns, "event_description", formatter, evaluator);
                String commandLine = getValue(row, columns, "process_commandline", formatter, evaluator);
                String result = getValue(row, columns, "result", formatter, evaluator);
                String severityValue = getValue(row, columns, "severity", formatter, evaluator);

                if (user.isBlank() && sourceIp.isBlank()
                        && destination.isBlank() && logSource.isBlank()
                        && description.isBlank() && commandLine.isBlank()
                        && result.isBlank() && severityValue.isBlank()) {
                    continue;
                }

                if (logSource.isBlank()) {
                    throw new IllegalArgumentException(
                            "Missing Log_Source in Excel row " + (i + 1)
                    );
                }

                SecurityEvent event = new SecurityEvent();

                event.setTimestamp(LocalDateTime.now());
                event.setUserIdentity(
                        user.isBlank() ? "UNKNOWN" : user
                );
                event.setSourceIp(
                        sourceIp.isBlank() ? "0.0.0.0" : sourceIp
                );
                event.setEventType(logSource);
                event.setAction(result);
                event.setSeverity(parseSeverity(severityValue));

                Map<String, Object> rawData = new LinkedHashMap<>();

                rawData.put("destination", destination);
                rawData.put("event_description", description);
                rawData.put("process_commandline", commandLine);
                rawData.put("result", result);
                rawData.put("ingestion_source", file.getOriginalFilename());
                rawData.put("row_number", i + 1);

                event.setRawData(rawData);

                // Imported Excel records are not synthetic.
                event.setSynthetic(false);

                events.add(event);
            }

            if (events.isEmpty()) {
                throw new IllegalArgumentException(
                        "No valid event records were found in the Excel file."
                );
            }

            eventRepository.saveAll(events);

            Map<String, Object> response = new LinkedHashMap<>();

            response.put("status", "success");
            response.put("message", "Excel events successfully imported.");
            response.put("fileName", file.getOriginalFilename());
            response.put("ingestedEventsCount", events.size());

            return response;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error processing Excel file: " + e.getMessage(), e
            );
        }
    }

    private String getValue(
            Row row,
            Map<String, Integer> columns,
            String columnName,
            DataFormatter formatter,
            FormulaEvaluator evaluator) {

        Integer index = columns.get(columnName);

        if (index == null) {
            return "";
        }

        Cell cell = row.getCell(index);

        if (cell == null) {
            return "";
        }

        return formatter.formatCellValue(cell, evaluator).trim();
    }

    private EventSeverity parseSeverity(String value) {

        if (value == null || value.isBlank()) {
            return EventSeverity.INFO;
        }

        String normalized = value.trim().toUpperCase(Locale.ROOT);

        return switch (normalized) {
            case "CRITICAL", "CRIT", "SEVERE" -> EventSeverity.CRITICAL;
            case "HIGH", "ERROR" -> EventSeverity.HIGH;
            case "MEDIUM", "MODERATE", "WARNING", "WARN" -> EventSeverity.MEDIUM;
            case "LOW" -> EventSeverity.LOW;
            case "INFO", "INFORMATIONAL", "INFORMATION" -> EventSeverity.INFO;
            default -> throw new IllegalArgumentException(
                    "Unsupported severity: " + value
            );
        };
    }
}
