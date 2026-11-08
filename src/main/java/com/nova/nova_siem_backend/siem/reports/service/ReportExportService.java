package com.nova.nova_siem_backend.siem.reports.service;

import com.nova.nova_siem_backend.siem.common.exception.BusinessException;
import com.nova.nova_siem_backend.siem.reports.dto.ReportResponseDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ReportExportService {

    public byte[] exportToExcel(ReportResponseDTO reportData) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Report Data");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Report ID");
            headerRow.createCell(1).setCellValue("Title");
            headerRow.createCell(2).setCellValue("Type");
            headerRow.createCell(3).setCellValue("Author");
            headerRow.createCell(4).setCellValue("Generated At");

            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(reportData.getReportId());
            dataRow.createCell(1).setCellValue(reportData.getTitle());
            dataRow.createCell(2).setCellValue(reportData.getType());
            dataRow.createCell(3).setCellValue(reportData.getAuthor());
            dataRow.createCell(4).setCellValue(reportData.getGeneratedAt() != null ? reportData.getGeneratedAt().toString() : "");

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new BusinessException("Failed to export report to Excel: " + e.getMessage());
        }
    }
}