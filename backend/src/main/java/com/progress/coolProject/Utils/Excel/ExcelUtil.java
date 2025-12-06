package com.progress.coolProject.Utils.Excel;

import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.web.multipart.MultipartFile;

@UtilityClass
public final class ExcelUtil {
    public static String validateExcelFilesAndGetFileNames(MultipartFile... excelFiles) {
        StringBuilder combinedFileNames = new StringBuilder();
        for (MultipartFile file : excelFiles) {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("One or more required Excel files are missing or empty.");
            }
            String fileName = file.getOriginalFilename();
            if (fileName == null) {
                throw new RuntimeException("File name of one or more files is invalid");
            }
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            if (!fileExtension.equals("xlsx") && !fileExtension.equals("xls")) {
                throw new RuntimeException("Only Excel files (.xlsx or .xls) are allowed. Right now provided file: " + fileName + " has invalid extension.");
            }
            String contentType = file.getContentType();
            if (contentType == null ||
                    (
                            !contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") &&
                            !contentType.equals("application/vnd.ms-excel")
                    )) {
                throw new RuntimeException("Invalid file type. Please upload a valid Excel file. Here provided file: " + fileName + " has invalid content type: " + contentType);
            }
            combinedFileNames.append(fileName).append(", ");
        }
        combinedFileNames.delete(combinedFileNames.length() - 2, combinedFileNames.length());
        return combinedFileNames.toString();
    }

    public static boolean isRowEmpty(Row row) {
        for (int i = 0; i < 4; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && !isCellEmpty(cell)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isCellEmpty(Cell cell) {
        if (cell == null) return true;
        if (cell.getCellType() == CellType.BLANK) return true;
        if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty()) return true;
        return false;
    }

    public static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    public static Integer getCellValueAsInteger(Cell cell) throws Exception {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().trim();
                return value.isEmpty() ? null : Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            throw new Exception("Invalid ledger number format: " + getCellValueAsString(cell));
        }
        return null;
    }

    public static Double getCellValueAsDouble(Cell cell) throws Exception {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().trim();
                return value.isEmpty() ? null : Double.parseDouble(value);
            }
        } catch (NumberFormatException e) {
            throw new Exception("Invalid number format: " + getCellValueAsString(cell));
        }
        return null;
    }
}
