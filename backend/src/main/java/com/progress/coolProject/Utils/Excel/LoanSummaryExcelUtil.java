package com.progress.coolProject.Utils.Excel;

import com.progress.coolProject.DTO.Excel.AccountSummaryDTO;
import com.progress.coolProject.DTO.Excel.MemberAccountDTO;
import com.progress.coolProject.Enums.AccountType;
import com.progress.coolProject.Enums.LoanCategory;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class LoanSummaryExcelUtil {

    private static final int HEADER_ROW_INDEX = 1; // Row 2 in Excel (0-indexed)
    private static final int DATA_START_ROW = 2;   // Row 3 in Excel (0-indexed)

    private static final String[] EXPECTED_HEADERS_FOR_SUMMARY_EXCEL = {
            "Code", "Description", "Opening", "Type", "Debit", "Credit", "Balance", "Type"
    };

    private static final String[] EXPECTED_HEADERS = {
            "Account No", "Description", "Opening", "Type", "Debit", "Credit", "Balance", "Type"
    };

    /**
     * Extract loan summary data from Excel sheet
     * @param sheet The Excel sheet containing loan summary data
     * @return HashMap with Code as key and AccountSummaryDTO as value
     */
    public static HashMap<LoanCategory, AccountSummaryDTO> extractLoanSummaryData(Sheet sheet) throws Exception {
        validateHeaders(sheet, EXPECTED_HEADERS_FOR_SUMMARY_EXCEL);

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

    public static HashMap<String, MemberAccountDTO> extractLoanMemberData(Sheet sheet) throws Exception {
        validateHeaders(sheet, EXPECTED_HEADERS);

        HashMap<String, MemberAccountDTO> resultMap = new HashMap<>();
        List<String> errors = new ArrayList<>();

        int lastRowNum = sheet.getLastRowNum();
        for (int i = DATA_START_ROW; i <= lastRowNum - 5; i++) {
            Row row = sheet.getRow(i);
            if (row == null || ExcelUtil.isRowEmpty(row)) {
                continue;
            }

            try {
                MemberAccountDTO dto = parseMemberRow(row);
                if (dto.getAccountNumber() == null || dto.getAccountNumber().trim().isEmpty()) {
                    errors.add("Row " + (i + 1) + ": Account number is required");
                    continue;
                }

                Optional<LoanCategory> loanCategory = LoanCategory.findByAccountNumber(dto.getAccountNumber());
                if (loanCategory.isEmpty()) {
                    errors.add("Row " + (i + 1) + ": Unable to resolve loan category from account number " + dto.getAccountNumber());
                    continue;
                }
                dto.setLoanCategory(loanCategory.get());

                resultMap.put(dto.getAccountNumber(), dto);
            } catch (Exception e) {
                errors.add("Row " + (i + 1) + ": " + e.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            throw new Exception("Validation errors found:\n" + String.join("\n", errors));
        }
        return resultMap;
    }

    public static HashMap<LoanCategory, AccountSummaryDTO> getSummary(HashMap<String, MemberAccountDTO> memberData) {
        HashMap<LoanCategory, AccountSummaryDTO> result = new HashMap<>();

        if (memberData == null || memberData.isEmpty()) {
            return result;
        }

        HashMap<LoanCategory, Aggregate> aggregateByCategory = new HashMap<>();

        for (Map.Entry<String, MemberAccountDTO> entry : memberData.entrySet()) {
            MemberAccountDTO member = entry.getValue();
            if (member == null || member.getLoanCategory() == null) {
                continue;
            }

            Aggregate aggregate = aggregateByCategory.computeIfAbsent(member.getLoanCategory(), k -> new Aggregate());
            aggregate.openingSigned += toSignedValue(member.getOpening(), member.getOpeningType());
            aggregate.debit += defaultZero(member.getDebit());
            aggregate.credit += defaultZero(member.getCredit());
            aggregate.balanceSigned += toSignedValue(member.getBalance(), member.getBalanceType());

            if (isNonZero(member.getBalance())) {
                aggregate.memberCount++;
            }
        }

        for (Map.Entry<LoanCategory, Aggregate> entry : aggregateByCategory.entrySet()) {
            LoanCategory category = entry.getKey();
            Aggregate aggregate = entry.getValue();

            AccountSummaryDTO dto = new AccountSummaryDTO();
            dto.setCode(category.getCode());
            dto.setDescription(category.getLoanTypeName());
            dto.setMemberCount(aggregate.memberCount);
            dto.setOpeningAmount(Math.abs(aggregate.openingSigned));
            dto.setOpeningType(getTypeFromSignedValue(aggregate.openingSigned));
            dto.setDebit(aggregate.debit);
            dto.setCredit(aggregate.credit);
            dto.setBalance(Math.abs(aggregate.balanceSigned));
            dto.setBalanceType(getTypeFromSignedValue(aggregate.balanceSigned));
            dto.setLoanCategory(category);

            result.put(category, dto);
        }

        return result;
    }

    private static void validateHeaders(Sheet sheet, String[] expectedHeaders) throws Exception {
        Row headerRow = sheet.getRow(HEADER_ROW_INDEX);
        if (headerRow == null) {
            throw new Exception("Header row not found at row " + (HEADER_ROW_INDEX + 1));
        }

        List<String> errors = new ArrayList<>();

        for (int i = 0; i < expectedHeaders.length; i++) {
            Cell cell = headerRow.getCell(i);
            String actualHeader = ExcelUtil.getCellValueAsString(cell);

            if (actualHeader == null || actualHeader.trim().isEmpty()) {
                errors.add("Column " + (i + 1) + ": Header is missing. Expected: '" + expectedHeaders[i] + "'");
                continue;
            }

            String normalizedActual = actualHeader.trim().replaceAll("\\s+", " ");
            String normalizedExpected = expectedHeaders[i].trim().replaceAll("\\s+", " ");

            if (!normalizedActual.equalsIgnoreCase(normalizedExpected)) {
                errors.add("Column " + (i + 1) + ": Invalid header. Expected: '"
                        + expectedHeaders[i] + "', Found: '" + actualHeader + "'");
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

    private static MemberAccountDTO parseMemberRow(Row row) {
        MemberAccountDTO dto = new MemberAccountDTO();

        dto.setAccountNumber(ExcelUtil.getCellValueAsString(row.getCell(0)));
        dto.setDescription(ExcelUtil.getCellValueAsString(row.getCell(1)));
        dto.setOpening(readNumericValue(row.getCell(2)));
        dto.setOpeningType(readAccountType(row.getCell(3)));
        dto.setDebit(readNumericValue(row.getCell(4)));
        dto.setCredit(readNumericValue(row.getCell(5)));
        dto.setBalance(readNumericValue(row.getCell(6)));
        dto.setBalanceType(readAccountType(row.getCell(7)));

        return dto;
    }

    private static Double readNumericValue(Cell cell) {
        try {
            return ExcelUtil.getCellValueAsDouble(cell);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private static AccountType readAccountType(Cell cell) {
        String accountType = ExcelUtil.getCellValueAsString(cell);
        if (accountType == null || accountType.trim().isEmpty()) {
            return null;
        }
        return AccountType.fromCode(accountType);
    }

    private static double defaultZero(Double value) {
        return value == null ? 0.0 : value;
    }

    private static double toSignedValue(Double amount, AccountType type) {
        if (amount == null) {
            return 0.0;
        }
        if (type == AccountType.CR) {
            return -Math.abs(amount);
        }
        return Math.abs(amount);
    }

    private static AccountType getTypeFromSignedValue(double value) {
        return value < 0 ? AccountType.CR : AccountType.DR;
    }

    private static boolean isNonZero(Double value) {
        return value != null && Math.abs(value) > 0.0;
    }

    private static class Aggregate {
        double openingSigned;
        double debit;
        double credit;
        double balanceSigned;
        int memberCount;
    }
}
