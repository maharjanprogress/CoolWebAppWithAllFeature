package com.progress.coolProject.Utils.Excel;

import com.progress.coolProject.DTO.Excel.AccountSummaryDTO;
import com.progress.coolProject.Enums.AccountType;
import com.progress.coolProject.Enums.LoanCategory;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class LoanSummaryExcelUtil {

    private static final int HEADER_ROW_INDEX = 1; // Row 2 in Excel (0-indexed)
    private static final int DATA_START_ROW = 2;   // Row 3 in Excel (0-indexed)

    private static final String[] EXPECTED_HEADERS = {
            "Code", "Description", "Opening", "Type", "Debit", "Credit", "Balance", "Type"
    };

    /**
     * Extract loan summary data from Excel sheet
     * @param sheet The Excel sheet containing loan summary data
     * @return HashMap with Code as key and AccountSummaryDTO as value
     */
    public static HashMap<LoanCategory, AccountSummaryDTO> extractLoanSummaryData(Sheet sheet) throws Exception {
        validateHeaders(sheet);

        HashMap<LoanCategory, AccountSummaryDTO> resultMap = new HashMap<>();
        List<String> errors = new ArrayList<>();

        int lastRowNum = sheet.getLastRowNum();

        for (int i = DATA_START_ROW; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null || ExcelUtil.isRowEmpty(row)) {
                continue;
            }

            try {
                AccountSummaryDTO dto = parseRow(row, i + 1);

                if (!dto.isValid()) {
                    errors.add("Row " + (i + 1) + ": " + dto.getValidationError());
                    continue;
                }

                // Validate against LoanCategory enum
                Optional<LoanCategory> loanCategory = LoanCategory.findByCode(dto.getCode());
                if (loanCategory.isEmpty()) {
                    errors.add("Row " + (i + 1) + ": Invalid loan code " + dto.getCode());
                    continue;
                }
                dto.setLoanCategory(loanCategory.get());

                resultMap.put(loanCategory.get(), dto);

            } catch (Exception e) {
                errors.add("Row " + (i + 1) + ": " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            throw new Exception("Validation errors found:\n" + String.join("\n", errors));
        }

        return resultMap;
    }

    private static void validateHeaders(Sheet sheet) throws Exception {
        Row headerRow = sheet.getRow(HEADER_ROW_INDEX);
        if (headerRow == null) {
            throw new Exception("Header row not found at row " + (HEADER_ROW_INDEX + 1));
        }

        List<String> errors = new ArrayList<>();

        for (int i = 0; i < EXPECTED_HEADERS.length; i++) {
            Cell cell = headerRow.getCell(i);
            String actualHeader = ExcelUtil.getCellValueAsString(cell);

            if (actualHeader == null || actualHeader.trim().isEmpty()) {
                errors.add("Column " + (i + 1) + ": Header is missing. Expected: '" + EXPECTED_HEADERS[i] + "'");
                continue;
            }

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

    private static AccountSummaryDTO parseRow(Row row, int rowNumber) throws Exception {
        AccountSummaryDTO dto = new AccountSummaryDTO();

        // Column 0: Code
        dto.setCode(ExcelUtil.getCellValueAsInteger(row.getCell(0)));

        // Column 1: Description
        dto.setDescription(ExcelUtil.getCellValueAsString(row.getCell(1)));

        // Column 2: Opening Amount
        dto.setOpeningAmount(ExcelUtil.getCellValueAsDouble(row.getCell(2)));

        // Column 3: Opening Type (DR/CR)
        String openingTypeStr = ExcelUtil.getCellValueAsString(row.getCell(3));
        if (openingTypeStr != null && !openingTypeStr.trim().isEmpty()) {
            dto.setOpeningType(AccountType.fromCode(openingTypeStr));
        }

        // Column 4: Debit
        dto.setDebit(ExcelUtil.getCellValueAsDouble(row.getCell(4)));

        // Column 5: Credit
        dto.setCredit(ExcelUtil.getCellValueAsDouble(row.getCell(5)));

        // Column 6: Balance
        dto.setBalance(ExcelUtil.getCellValueAsDouble(row.getCell(6)));

        // Column 7: Balance Type (DR/CR)
        String balanceTypeStr = ExcelUtil.getCellValueAsString(row.getCell(7));
        if (balanceTypeStr != null && !balanceTypeStr.trim().isEmpty()) {
            dto.setBalanceType(AccountType.fromCode(balanceTypeStr));
        }

        return dto;
    }
}