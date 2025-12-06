package com.progress.coolProject.Utils.Excel;


import com.progress.coolProject.DTO.Excel.LoanAccountAgeingDTO;
import com.progress.coolProject.Enums.LoanCategory;
import com.progress.coolProject.Enums.LoanPayerCategory;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class LoanAccountAgeingExcelUtil {

    private static final int HEADER_ROW_INDEX = 1; // Row 2 in Excel (0-indexed)
    private static final int DATA_START_ROW = 2;   // Row 3 in Excel (0-indexed)

    private static final String[] EXPECTED_HEADERS = {
            "Account No",
            "Description",
            "Account Name",
            "Period",
            "Opening Date",
            "Matured Date",
            "Last Repayment Date",
            "Current Date",
            "Lapsed Days",
            "Loan Amount",
            "Balance Amount",
            "1-30",
            "31-90",
            "91-180",
            "181-270",
            "271-365",
            "366-730",
            "Above 730",
            "Below 1"
    };

    /**
     * Extract loan account ageing data from Excel sheet
     * @param sheet The Excel sheet containing loan account ageing data
     * @return HashMap with Account Number as key and LoanAccountAgeingDTO as value
     */
    public static HashMap<String, LoanAccountAgeingDTO> extractLoanAccountAgeingData(Sheet sheet) throws Exception {

        validateHeaders(sheet);

        HashMap<String, LoanAccountAgeingDTO> resultMap = new HashMap<>();
        List<String> errors = new ArrayList<>();

        int lastRowNum = sheet.getLastRowNum();

        for (int i = DATA_START_ROW; i < lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) {
                continue; // Skip empty rows
            }

            try {
                LoanAccountAgeingDTO dto = parseRow(row, i + 1);

                // Validate the row
                if (!dto.isValid()) {
                    errors.add("Row " + (i + 1) + ": " + dto.getValidationError());
                    continue;
                }

                // Add to map with account number as key
                resultMap.put(dto.getAccountNo(), dto);

            } catch (Exception e) {
                errors.add("Row " + (i + 1) + ": " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            throw new RuntimeException("Validation errors found:\n" + String.join("\n", errors));
        }

        return resultMap;
    }

    /**
     * Validate that the Excel sheet has the correct headers
     * @param sheet The Excel sheet to validate
     * @throws Exception if headers are invalid or missing
     */
    private static void validateHeaders(Sheet sheet) throws Exception {
        Row headerRow = sheet.getRow(HEADER_ROW_INDEX);
        if (headerRow == null) {
            throw new Exception("Header row not found at row " + (HEADER_ROW_INDEX + 1));
        }

        List<String> errors = new ArrayList<>();

        for (int i = 0; i < EXPECTED_HEADERS.length; i++) {
            Cell cell = headerRow.getCell(i);
            String actualHeader = getCellValueAsString(cell);

            if (actualHeader == null || actualHeader.trim().isEmpty()) {
                errors.add("Column " + (i + 1) + ": Header is missing. Expected: '" + EXPECTED_HEADERS[i] + "'");
                continue;
            }

            // Normalize and compare (case-insensitive, trim whitespace)
            String normalizedActual = actualHeader.trim().replaceAll("\\s+", " ");
            String normalizedExpected = EXPECTED_HEADERS[i].trim().replaceAll("\\s+", " ");

            if (!normalizedActual.equalsIgnoreCase(normalizedExpected)) {
                errors.add("Column " + (i + 1) + ": Invalid header. Expected: '"
                        + EXPECTED_HEADERS[i] + "', Found: '" + actualHeader + "'");
            }
        }

        if (!errors.isEmpty()) {
            throw new Exception("Header validation failed:\n" + String.join("\n", errors));
        }
    }

    private static LoanAccountAgeingDTO parseRow(Row row, int rowNumber) throws Exception {
        LoanAccountAgeingDTO dto = new LoanAccountAgeingDTO();

        // Column 0: Account No
        dto.setAccountNo(getCellValueAsString(row.getCell(0)));

        // Column 1: Description
        dto.setDescription(getCellValueAsString(row.getCell(1)));

        // Column 2: Account Name (LoanType)
        String loanTypeName = getCellValueAsString(row.getCell(2));
        Optional<LoanCategory> loanType = LoanCategory.findByLoanTypeName(loanTypeName);
        if (loanType.isEmpty()) {
            throw new Exception("Invalid loan type: " + loanTypeName);
        }
        dto.setAccountName(loanType.get());

        // Column 3: Period
        dto.setPeriod(getCellValueAsString(row.getCell(3)));

        // Column 4: Opening Date
        dto.setOpeningDate(getCellValueAsDate(row.getCell(4)));

        // Column 5: Matured Date
        dto.setMaturedDate(getCellValueAsDate(row.getCell(5)));

        // Column 6: Last Repayment Date
        dto.setLastRepaymentDate(getCellValueAsDate(row.getCell(6)));

        // Column 7: Current Date
        dto.setCurrentDate(getCellValueAsDate(row.getCell(7)));

        // Column 8: Lapsed Days
        dto.setLapsedDays(getCellValueAsInteger(row.getCell(8)));

        // Column 9: Loan Amount
        dto.setLoanAmount(getCellValueAsDouble(row.getCell(9)));

        // Column 10: Balance Amount
        dto.setBalanceAmount(getCellValueAsDouble(row.getCell(10)));

        for(int i = 11 ; i <=18 ; i++){
            Double balanceAmount = getCellValueAsDouble(row.getCell(i));
            if (balanceAmount == null) {
                continue;
            } else if (!balanceAmount.equals(dto.getBalanceAmount())) {
                throw new Exception("Invalid balance amount: " + balanceAmount + " Expected: " + dto.getBalanceAmount()
                + " , In the account Number: " + dto.getAccountNo());
            }

            switch (i) {
                case 11 -> dto.setPaymentType(LoanPayerCategory.UPTO_ONE_MONTH);
                case 12 -> dto.setPaymentType(LoanPayerCategory.ONE_TO_THREE_MONTHS);
                case 13 -> dto.setPaymentType(LoanPayerCategory.THREE_TO_SIX_MONTHS);
                case 14 -> dto.setPaymentType(LoanPayerCategory.SIX_TO_NINE_MONTHS);
                case 15 -> dto.setPaymentType(LoanPayerCategory.NINE_TO_TWELVE_MONTHS);
                case 16 -> dto.setPaymentType(LoanPayerCategory.ONE_TO_TWO_YEARS);
                case 17 -> dto.setPaymentType(LoanPayerCategory.ABOVE_TWO_YEARS);
                case 18 -> dto.setPaymentType(LoanPayerCategory.BELOW_ONE);
                default -> throw new Exception("Invalid column number for loan payers category: " + i);
            }
        }
        return dto;
    }

    private static boolean isRowEmpty(Row row) {
        // Check first 3 columns to determine if row is empty
        for (int i = 0; i < 3; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && !isCellEmpty(cell)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isCellEmpty(Cell cell) {
        if (cell == null) return true;
        if (cell.getCellType() == CellType.BLANK) return true;
        if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty()) return true;
        return false;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    yield String.valueOf((long) cell.getNumericCellValue());
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> null;
        };
    }

    private static Integer getCellValueAsInteger(Cell cell) throws Exception {
        if (cell == null || isCellEmpty(cell)) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().trim();
                return value.isEmpty() ? null : Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            throw new Exception("Invalid integer format: " + getCellValueAsString(cell));
        }
        return null;
    }

    private static Double getCellValueAsDouble(Cell cell) throws Exception {
        if (cell == null || isCellEmpty(cell)) return null;
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

    private static LocalDate getCellValueAsDate(Cell cell) throws Exception {
        if (cell == null || isCellEmpty(cell)) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            } else if (cell.getCellType() == CellType.STRING) {
                //todo: check if this hits...
                // Handle string date format if needed
                String dateStr = cell.getStringCellValue().trim();
                // You can add custom date parsing logic here if needed
                return null;
            }
        } catch (Exception e) {
            throw new Exception("Invalid date format: " + getCellValueAsString(cell));
        }
        return null;
    }
}