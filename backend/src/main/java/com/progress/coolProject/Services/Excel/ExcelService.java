package com.progress.coolProject.Services.Excel;

import com.progress.coolProject.DTO.Excel.ExcelRowDTO;
import com.progress.coolProject.DTO.Excel.ProgressUpdate;
import com.progress.coolProject.Entity.Excel.ProcessingJob;
import com.progress.coolProject.Entity.User;
import com.progress.coolProject.Enums.JobStatus;
import com.progress.coolProject.Enums.TrialBalanceEnum;
import com.progress.coolProject.Repo.Excel.ProcessingJobRepository;
import com.progress.coolProject.Services.Impl.Excel.IExcelService;
import com.progress.coolProject.Utils.Excel.ExcelTrialBalanceExcelRowHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelService implements IExcelService {
    private final ProcessingJobRepository jobRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String OUTPUT_DIRECTORY = "/opt/coolwebapp/excelFile";
    private static final int HEADER_ROW_INDEX = 1;
    private static final int DATA_START_ROW = 2;

    @Override
    public ProcessingJob startProcessing(MultipartFile file, User user) {
        // Check if user already has an active job
        Optional<ProcessingJob> existingJob = jobRepository.findFirstByUserAndStatusInOrderByCreatedAtDesc(
                user,
                List.of(JobStatus.PENDING, JobStatus.PROCESSING)
        );

        if (existingJob.isPresent()) {
            throw new RuntimeException("You already have a file being processed. Please wait.");
        }

        // Validate file
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new RuntimeException("File name is null");
        }

        // Check file extension
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!fileExtension.equals("xlsx") && !fileExtension.equals("xls")) {
            throw new RuntimeException("Only Excel files (.xlsx or .xls) are allowed");
        }

        // Validate MIME type
        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") &&
                        !contentType.equals("application/vnd.ms-excel"))) {
            throw new RuntimeException("Invalid file type. Please upload a valid Excel file.");
        }

        // Create job record
        ProcessingJob job = new ProcessingJob();
        job.setUser(user);
        job.setFileName(fileName);
        job.setStatus(JobStatus.PENDING);
        job.setStartedAt(LocalDateTime.now());
        job = jobRepository.save(job);

        // Process asynchronously
        processExcelAsync(job, file);

        return job;
    }

    @Override
    public ProcessingJob getActiveJob(User user) {
        return jobRepository.findFirstByUserAndStatusInOrderByCreatedAtDesc(
                user,
                List.of(JobStatus.PENDING, JobStatus.PROCESSING)
        ).orElse(null);
    }

    @Async
    public void processExcelAsync(ProcessingJob job, MultipartFile file) {
        Workbook workbook = null;
        try {
            // Update status to processing
            job.setStatus(JobStatus.PROCESSING);
            jobRepository.save(job);
            sendProgress(job, 0, "Starting processing...");

            // Read Excel file
            sendProgress(job, 10, "Reading Excel file...");
            workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            // Validate headers
            sendProgress(job, 20, "Validating file structure...");
            validateHeaders(sheet);

            // Parse and validate data
            sendProgress(job, 30, "Validating data...");
            Map<TrialBalanceEnum, ExcelRowDTO> rows = parseAndValidateData(sheet);

            if (rows.isEmpty()) {
                throw new RuntimeException("No valid data found in the Excel file");
            }

            // Create output directory if not exists
            sendProgress(job, 50, "Preparing output directory...");
            createOutputDirectory();

            // Generate Nepali Excel file
            sendProgress(job, 60, "Generating Nepali translation...");
            String outputFilePath = generateNepaliExcel(rows, job.getUser().getUserName());

            // Complete processing
            sendProgress(job, 100, "Completed! File saved at: " + outputFilePath);

            job.setStatus(JobStatus.COMPLETED);
            job.setProgress(100);
            job.setCompletedAt(LocalDateTime.now());
            job.setCurrentStep("File processed successfully. Output: " + outputFilePath);
            jobRepository.save(job);

            sendStatusUpdate(job, "Processing completed successfully! File saved at: " + outputFilePath);

        } catch (Exception e) {
            log.error(e.getMessage(),e);
            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(e.getMessage());
            job.setCompletedAt(LocalDateTime.now());
            jobRepository.save(job);

            sendStatusUpdate(job, "Processing failed: " + e.getMessage());
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    log.error(e.getMessage(),e);
                }
            }
        }
    }

    private void validateHeaders(Sheet sheet) throws Exception {
        Row headerRow = sheet.getRow(HEADER_ROW_INDEX);
        if (headerRow == null) {
            throw new Exception("Header row not found");
        }

        // Expected headers
        String[] expectedHeaders = {"Ledger", "Description", "Debit", "Credit"};

        for (int i = 0; i < expectedHeaders.length; i++) {
            Cell cell = headerRow.getCell(i);
            if (cell == null || !getCellValueAsString(cell).trim().equalsIgnoreCase(expectedHeaders[i])) {
                throw new Exception("Invalid header at column " + (i + 1) +
                        ". Expected: " + expectedHeaders[i] + ", Found: " +
                        (cell != null ? getCellValueAsString(cell) : "null"));
            }
        }
    }

    private Map<TrialBalanceEnum, ExcelRowDTO> parseAndValidateData(Sheet sheet) throws Exception {
        Map<TrialBalanceEnum, ExcelRowDTO> rowsMap = new HashMap<>();
        List<String> errors = new ArrayList<>();

        int lastRowNum = sheet.getLastRowNum();

        for (int i = DATA_START_ROW; i < lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) {
                continue; // Skip empty rows
            }

            try {
                ExcelRowDTO dto = parseRow(row, i + 1);

                // Validate the row
                if (!dto.isValid()) {
                    errors.add("Row " + (i + 1) + ": " + dto.getValidationError());
                    continue;
                }

                // Validate against enum
                Optional<TrialBalanceEnum> enumEntry = TrialBalanceEnum.findByLedgerNo(dto.getLedgerNo());
                if (enumEntry.isEmpty()) {
                    errors.add("Row " + (i + 1) + ": Ledger number " + dto.getLedgerNo() +
                            " not found in system");
                    continue;
                }

                // Verify description matches
                TrialBalanceEnum entry = enumEntry.get();
                String normalizedDesc = dto.getLedgerDescription().trim().toLowerCase();
                String normalizedEnumDesc = entry.getDescriptionEn().toLowerCase();

                if (!normalizedDesc.equals(normalizedEnumDesc)) {
                    errors.add("Row " + (i + 1) + ": Description mismatch for ledger " +
                            dto.getLedgerNo() + ". Expected: '" + entry.getDescriptionEn() +
                            "', Found: '" + dto.getLedgerDescription() + "'");
                    continue;
                }

                // Add to map with enum as key
                rowsMap.put(entry, dto);

            } catch (Exception e) {
                errors.add("Row " + (i + 1) + ": " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            throw new Exception("Validation errors found:\n" + String.join("\n", errors));
        }

        return rowsMap;
    }

    private ExcelRowDTO parseRow(Row row, int rowNumber) throws Exception {
        ExcelRowDTO dto = new ExcelRowDTO();
        dto.setRowNumber(rowNumber);

        // Ledger No (Column 0)
        Cell ledgerCell = row.getCell(0);
        if (ledgerCell != null) {
            dto.setLedgerNo(getCellValueAsInteger(ledgerCell));
        }

        // Description (Column 1)
        Cell descCell = row.getCell(1);
        if (descCell != null) {
            dto.setLedgerDescription(getCellValueAsString(descCell));
        }

        // Debit (Column 2)
        Cell debitCell = row.getCell(2);
        if (debitCell != null && !isCellEmpty(debitCell)) {
            dto.setDebit(getCellValueAsDouble(debitCell));
        }

        // Credit (Column 3)
        Cell creditCell = row.getCell(3);
        if (creditCell != null && !isCellEmpty(creditCell)) {
            dto.setCredit(getCellValueAsDouble(creditCell));
        }

        return dto;
    }

    private String generateNepaliExcel(Map<TrialBalanceEnum, ExcelRowDTO> rowsMap, String username) throws IOException {
        ExcelTrialBalanceExcelRowHelper excel = new ExcelTrialBalanceExcelRowHelper(rowsMap);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Trial Balance (Nepali)");

        // Create header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Ledger", "Description", "Debit", "Credit"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Populate data rows with Nepali descriptions
        int rowIdx = 1;
        for (Map.Entry<TrialBalanceEnum, ExcelRowDTO> entry : rowsMap.entrySet()) {
            TrialBalanceEnum enumEntry = entry.getKey();
            ExcelRowDTO dto = entry.getValue();

            Row row = sheet.createRow(rowIdx++);

            // Ledger No
            row.createCell(0).setCellValue(dto.getLedgerNo());

            // Nepali Description (directly from enum)
            row.createCell(1).setCellValue(enumEntry.getDescriptionNp());

            // Debit
            if (dto.getDebit() != null && dto.getDebit() > 0) {
                row.createCell(2).setCellValue(dto.getDebit());
            }

            // Credit
            if (dto.getCredit() != null && dto.getCredit() > 0) {
                row.createCell(3).setCellValue(dto.getCredit());
            }
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Generate output file path
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = username + "_trial_balance_" + timestamp + ".xlsx";
        String filePath = OUTPUT_DIRECTORY + File.separator + fileName;

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }

        return filePath;
    }

    private void createOutputDirectory() throws IOException {
        Path path = Paths.get(OUTPUT_DIRECTORY);
        log.info("Checking/Creating output directory at: {}", path);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
            log.info("Created output directory: {}", path);
        }
    }

    private boolean isRowEmpty(Row row) {
        for (int i = 0; i < 4; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && !isCellEmpty(cell)) {
                return false;
            }
        }
        return true;
    }

    private boolean isCellEmpty(Cell cell) {
        if (cell == null) return true;
        if (cell.getCellType() == CellType.BLANK) return true;
        if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty()) return true;
        return false;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    private Integer getCellValueAsInteger(Cell cell) throws Exception {
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

    private Double getCellValueAsDouble(Cell cell) throws Exception {
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

    private void sendProgress(ProcessingJob job, int progress, String message) {
        job.setProgress(progress);
        job.setCurrentStep(message);
        jobRepository.save(job);

        sendStatusUpdate(job, message);
    }

    private void sendStatusUpdate(ProcessingJob job, String message) {
        ProgressUpdate update = new ProgressUpdate(
                job.getId(),
                job.getProgress(),
                message,
                job.getStatus()
        );

        // IMPORTANT: Use the USERNAME (not userId) to send to specific user
        messagingTemplate.convertAndSendToUser(
                job.getUser().getUserName(),  // This must match the username in JWT
                "/queue/excel-status",
                update
        );

        System.out.println("Sent progress to user: " + job.getUser().getUserName()
                + " - " + job.getProgress() + "%");
    }
}