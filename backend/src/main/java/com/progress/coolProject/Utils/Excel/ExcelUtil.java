package com.progress.coolProject.Utils.Excel;

import lombok.experimental.UtilityClass;
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
}
