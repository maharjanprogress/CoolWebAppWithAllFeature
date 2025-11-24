package com.progress.coolProject.Services.Excel;

import com.progress.coolProject.DTO.Excel.ExcelRowDTO;
import com.progress.coolProject.DTO.Excel.ProgressUpdate;
import com.progress.coolProject.DTO.ResponseDTO;
import com.progress.coolProject.Entity.Excel.ProcessingJob;
import com.progress.coolProject.Entity.User;
import com.progress.coolProject.Enums.JobStatus;
import com.progress.coolProject.Enums.TrialBalanceEnum;
import com.progress.coolProject.Repo.Excel.ProcessingJobRepository;
import com.progress.coolProject.Services.Impl.Excel.IExcelService;
import com.progress.coolProject.StringConstants;
import com.progress.coolProject.Utils.Excel.ExcelTrialBalanceExcelRowHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.text.XDDFTextRun;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExcelService implements IExcelService {
    private final ProcessingJobRepository jobRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private static final String OUTPUT_EXCEL_DIRECTORY = "excelFile";
    private static final String OUTPUT_POWERPOINT_DIRECTORY = "pptFile";
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
                List.of(JobStatus.PENDING, JobStatus.PROCESSING, JobStatus.COMPLETED)
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

            // Create output directories
            sendProgress(job, 40, "Preparing output directories...");
            createOutputDirectory(OUTPUT_EXCEL_DIRECTORY);
            createOutputDirectory(OUTPUT_POWERPOINT_DIRECTORY);

            // Generate Nepali Excel file with 3 sheets
            sendProgress(job, 50, "Generating Nepali translation with multiple sheets...");
            String outputExcelPath = generateNepaliExcelWithSheets(rows, job.getUser().getUserName());
            job.setProcessedExcelFilePath(outputExcelPath);
            sendProgress(job, 65, outputExcelPath);

            // Generate PowerPoint presentation
            sendProgress(job, 70, "Generating PowerPoint presentation...");
            String outputPptPath = generatePowerPoint(rows, job.getUser().getUserName());
            job.setProcessedPowerpointFilePath(outputPptPath);
            sendProgress(job, 85, outputPptPath);

            // Complete processing
            job.setStatus(JobStatus.COMPLETED);
            job.setProgress(100);
            job.setCompletedAt(LocalDateTime.now());
            job.setCurrentStep("Files processed successfully!");
            jobRepository.save(job);

            sendStatusUpdate(job, "Processing completed successfully! Excel: " + outputExcelPath + " | PPT: " + outputPptPath);

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

    /**
     * Generate Excel file with 3 sheets:
     * 1. All rows (Nepali)
     * 2. Credit only rows
     * 3. Debit only rows
     */
    private String generateNepaliExcelWithSheets(Map<TrialBalanceEnum, ExcelRowDTO> rowsMap, String username) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        // Create header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // Sheet 1: All rows (Nepali)
        createSheet(workbook, "All Data (Nepali)", rowsMap, headerStyle, null);

        // Sheet 2: Credit only rows
        Map<TrialBalanceEnum, ExcelRowDTO> creditRows = rowsMap.entrySet().stream()
                .filter(entry -> entry.getValue().getCredit() != null && entry.getValue().getCredit() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        createSheet(workbook, "Credit Only", creditRows, headerStyle, null);

        // Sheet 3: Debit only rows
        Map<TrialBalanceEnum, ExcelRowDTO> debitRows = rowsMap.entrySet().stream()
                .filter(entry -> entry.getValue().getDebit() != null && entry.getValue().getDebit() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        createSheet(workbook, "Debit Only", debitRows, headerStyle, null);

        // Generate output file path
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = username + "_trial_balance_" + timestamp + ".xlsx";
        String filePath = Paths.get(StringConstants.FILE_STORAGE_PATH,
                OUTPUT_EXCEL_DIRECTORY,
                fileName).toString();

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }

        return Paths.get(StringConstants.FILE_ACCESS_URL, OUTPUT_EXCEL_DIRECTORY, fileName).toString();
    }

    /**
     * Helper method to create a sheet with given data
     */
    private void createSheet(Workbook workbook, String sheetName,
                             Map<TrialBalanceEnum, ExcelRowDTO> data,
                             CellStyle headerStyle, Integer maxRows) {
        ExcelTrialBalanceExcelRowHelper excel = new ExcelTrialBalanceExcelRowHelper(data);
        Sheet sheet = workbook.createSheet(sheetName);

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Ledger", "Description", "Debit", "Credit"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Populate data rows
        int rowIdx = 1;
        int count = 0;
        for (Map.Entry<TrialBalanceEnum, ExcelRowDTO> entry : data.entrySet()) {
            if (maxRows != null && count >= maxRows) {
                break;
            }

            TrialBalanceEnum enumEntry = entry.getKey();
            ExcelRowDTO dto = entry.getValue();

            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(dto.getLedgerNo());
            row.createCell(1).setCellValue(enumEntry.getDescriptionNp());

            if (dto.getDebit() != null && dto.getDebit() > 0) {
                row.createCell(2).setCellValue(dto.getDebit());
            }

            if (dto.getCredit() != null && dto.getCredit() > 0) {
                row.createCell(3).setCellValue(dto.getCredit());
            }
            count++;
        }
        Row row = sheet.createRow(rowIdx+1);
        row.createCell(1).setCellValue("Total");
        double result = excel.getDebit(TrialBalanceEnum.KASKUN_REGULAR)+
                excel.getDebit(TrialBalanceEnum.KRISHI_BIKASH_BANK)+
                excel.getDebit(TrialBalanceEnum.KASKUN_DAINIK) +
                excel.getDebit(TrialBalanceEnum.BANK_DEVIDEND_SAVING)+
                excel.getDebit(TrialBalanceEnum.NEPAL_INV_BANK)+
                excel.getDebit(TrialBalanceEnum.NATIONAL_COOPERATIVE);
        row.createCell(2).setCellValue(new BigDecimal(Double.toString(result)).toPlainString());

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    /**
     * Generate PowerPoint presentation with:
     * - Slide 1: Title slide
     * - Slide 2: First 5 rows from Credit sheet
     * - Slide 3: First 5 rows from Debit sheet
     */
    private String generatePowerPoint(Map<TrialBalanceEnum, ExcelRowDTO> rowsMap, String username) throws IOException {
        XMLSlideShow ppt = new XMLSlideShow();

        // Slide 1: Title Slide
        XSLFSlide titleSlide = ppt.createSlide();
        XSLFTextBox titleBox = titleSlide.createTextBox();
        titleBox.setAnchor(new Rectangle(50, 100, 600, 100));
        XSLFTextParagraph titlePara = titleBox.addNewTextParagraph();
        titlePara.setTextAlign(TextParagraph.TextAlign.CENTER);
        XSLFTextRun titleRun = titlePara.addNewTextRun();
        titleRun.setText("Nepali Thing");
        titleRun.setFontSize(44.0);
        titleRun.setBold(true);

        XSLFTextBox subtitleBox = titleSlide.createTextBox();
        subtitleBox.setAnchor(new Rectangle(50, 250, 600, 50));
        XSLFTextParagraph subtitlePara = subtitleBox.addNewTextParagraph();
        subtitlePara.setTextAlign(TextParagraph.TextAlign.CENTER);
        XSLFTextRun subtitleRun = subtitlePara.addNewTextRun();
        subtitleRun.setText("Presented by Progress");
        subtitleRun.setFontSize(24.0);

        // Filter credit and debit rows
        List<Map.Entry<TrialBalanceEnum, ExcelRowDTO>> creditRows = rowsMap.entrySet().stream()
                .filter(entry -> entry.getValue().getCredit() != null && entry.getValue().getCredit() > 0)
                .limit(5)
                .collect(Collectors.toList());

        List<Map.Entry<TrialBalanceEnum, ExcelRowDTO>> debitRows = rowsMap.entrySet().stream()
                .filter(entry -> entry.getValue().getDebit() != null && entry.getValue().getDebit() > 0)
                .limit(5)
                .collect(Collectors.toList());

        // Slide 2: Credit Data (First 5 rows)
        createDataSlide(ppt, "Credit Entries (Top 5)", creditRows, true);

        // Slide 3: Debit Data (First 5 rows)
        createDataSlide(ppt, "Debit Entries (Top 5)", debitRows, false);

        // Generate output file path
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = username + "_trial_balance_" + timestamp + ".pptx";
        String filePath = Paths.get(StringConstants.FILE_STORAGE_PATH,
                OUTPUT_POWERPOINT_DIRECTORY,
                fileName).toString();

        // Write to file
        try (FileOutputStream out = new FileOutputStream(filePath)) {
            ppt.write(out);
        } finally {
            ppt.close();
        }

        return Paths.get(StringConstants.FILE_ACCESS_URL, OUTPUT_POWERPOINT_DIRECTORY, fileName).toString();
    }

    /**
     * Create a slide with table data
     */
    private void createDataSlide(XMLSlideShow ppt, String title,
                                 List<Map.Entry<TrialBalanceEnum, ExcelRowDTO>> data,
                                 boolean isCredit) {
        XSLFSlide slide = ppt.createSlide();

        // Add title
        XSLFTextBox titleBox = slide.createTextBox();
        titleBox.setAnchor(new Rectangle(50, 20, 600, 50));
        XSLFTextParagraph titlePara = titleBox.addNewTextParagraph();
        titlePara.setTextAlign(TextParagraph.TextAlign.CENTER);
        XSLFTextRun titleRun = titlePara.addNewTextRun();
        titleRun.setText(title);
        titleRun.setFontSize(32.0);
        titleRun.setBold(true);

        // Create table (4 columns: Ledger, Description, Debit, Credit)
        int numRows = data.size() + 1; // +1 for header
        int numCols = 4;
        XSLFTable table = slide.createTable(numRows, numCols);
        table.setAnchor(new Rectangle(50, 100, 600, 300));

        // Set column widths
        table.setColumnWidth(0, 80);  // Ledger
        table.setColumnWidth(1, 300); // Description
        table.setColumnWidth(2, 110); // Debit
        table.setColumnWidth(3, 110); // Credit

        // Header row
        String[] headers = {"Ledger", "Description", "Debit", "Credit"};
        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(0, col);
            cell.setText(headers[col]);
            cell.setFillColor(new Color(79, 129, 189));
            XDDFTextRun run = cell.getTextBody().getParagraphs().getFirst().getTextRuns().getFirst();
            run.setBold(true);
            run.setFontColor(XDDFColor.from(125, 125, 125));
        }

        // Data rows
        int rowIdx = 1;
        for (Map.Entry<TrialBalanceEnum, ExcelRowDTO> entry : data) {
            TrialBalanceEnum enumEntry = entry.getKey();
            ExcelRowDTO dto = entry.getValue();

            table.getCell(rowIdx, 0).setText(String.valueOf(dto.getLedgerNo()));
            table.getCell(rowIdx, 1).setText(enumEntry.getDescriptionNp());

            if (dto.getDebit() != null && dto.getDebit() > 0) {
                table.getCell(rowIdx, 2).setText(String.format("%.2f", dto.getDebit()));
            } else {
                table.getCell(rowIdx, 2).setText("-");
            }

            if (dto.getCredit() != null && dto.getCredit() > 0) {
                table.getCell(rowIdx, 3).setText(String.format("%.2f", dto.getCredit()));
            } else {
                table.getCell(rowIdx, 3).setText("-");
            }

            rowIdx++;
        }
    }

    private void createOutputDirectory(String directoryName) throws IOException {
        Path path = Paths.get(StringConstants.FILE_STORAGE_PATH, directoryName);
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
                job.getStatus(),
                job.getProcessedExcelFilePath()!= null,
                job.getProcessedPowerpointFilePath()!= null
        );

        // IMPORTANT: Use the USERNAME (not userId) to send to specific user
        messagingTemplate.convertAndSendToUser(
                job.getUser().getUserName(),  // This must match the username in JWT
                "/queue/excel-status",
                ResponseDTO.success("Progress update", update)
        );

        System.out.println("Sent progress to user: " + job.getUser().getUserName()
                + " - " + job.getProgress() + "%");
    }
}