package com.nova.nova_siem_backend.siem.ingestion.validator;

import com.nova.nova_siem_backend.siem.common.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ExcelFileValidator {

    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("The uploaded file is empty or missing.");
        }

        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();

        boolean isExcel = (contentType != null &&
                (contentType.equals("application/vnd.ms-excel") ||
                        contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")))
                || (fileName != null && (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")));

        if (!isExcel) {
            throw new BusinessException("Invalid file format. Only Excel files (.xls, .xlsx) are supported.");
        }
    }
}