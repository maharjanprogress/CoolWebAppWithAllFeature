package com.progress.coolProject.DTO.Excel.Slides;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import com.progress.coolProject.DTO.Excel.LoanAccountAgeingDTO;
import com.progress.coolProject.DTO.Excel.OfficeConstants;
import com.progress.coolProject.Enums.LoanPayerCategory;
import com.progress.coolProject.Utils.Excel.ExcelLoanAgeingHelper;
import com.progress.coolProject.Utils.PowerPoint.PPTUtils;
import lombok.experimental.UtilityClass;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.text.TextAlignment;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;

import java.awt.*;
import java.util.stream.Collectors;

@UtilityClass
public final class SlideTwelve {
    // Column Headers
    public static final String COLUMN_DESCRIPTION = "";  // Empty first column header
    public static final String COLUMN_TOTAL_COUNT = "जम्मा संख्या";
    public static final String COLUMN_LOAN_TAKEN_COUNT = "ऋण लिने संख्या";
    public static final String COLUMN_LOAN_AMOUNT = "लिएको ऋण रकम";
    public static final String COLUMN_PERCENTAGE_TOTAL_LOAN = "प्रतिशत कूल ऋणमा";
    public static final String COLUMN_RECOVERY_RATE = "असुली दर";
    public static final String COLUMN_QUALITY = "केफियत";

    // Row Labels
    public static final String ROW_BOARD_COMMITTEE = "संचालक समिति";
    public static final String ROW_AUDIT_COMMITTEE = "लेखा सु. समिति";
    public static final String ROW_EMPLOYEES = "कर्मचारीहरु";

    private static final double NORMAL_FONT_SIZE = 20.0;
    private static final PresetColor NORMAL_FONT_COLOR = PresetColor.BLACK;
    private static final Color NORMAL_BORDER_COLOR = Color.BLACK;

    // Board Committee Calculation Methods
    public static Integer getBoardCommitteeTotalCount() {
        return OfficeConstants.boardMembers.size();
    }

    public static Integer getBoardCommitteeLoanTakenCount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory().values().stream()
                .filter(ex -> ex.getDescription() != null)
                .map(ex -> ex.getDescription().trim().toUpperCase())
                .filter(OfficeConstants.boardMembers::contains)
                .collect(Collectors.toSet())
                .size();
    }

    public static Double getBoardCommitteeLoanAmount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory().values().stream()
                .filter(ex -> ex.getDescription() != null)
                .filter(ex -> OfficeConstants.boardMembers
                        .contains(ex.getDescription().trim().toUpperCase()))
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
    }

    public static Double getBoardCommitteeLoanPercentage(ExcelLoanAgeingHelper loanHelper) {
        return (getBoardCommitteeLoanAmount(loanHelper) / loanHelper.getTotalBalance()) * 100;
    }

    public static Double getBoardCommitteeRecoveryRate(ExcelLoanAgeingHelper loanHelper) {
        Double goodPeopleTotalBalance = loanHelper.getFilteredPayCategory(
                        LoanPayerCategory.BELOW_ONE,
                        LoanPayerCategory.UPTO_ONE_MONTH,
                        LoanPayerCategory.ONE_TO_THREE_MONTHS
                )
                .values().stream()
                .filter(ex -> ex.getDescription() != null)
                .filter(ex -> OfficeConstants.boardMembers
                        .contains(ex.getDescription().trim().toUpperCase()))
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
        Double total = getBoardCommitteeLoanAmount(loanHelper);
        return (goodPeopleTotalBalance / total) * 100;
    }

    public static String getBoardCommitteeQuality() {
        return "";
    }

    // Audit Committee Calculation Methods
    public static Integer getAuditCommitteeTotalCount() {
        return OfficeConstants.auditCommitteeMembers.size();
    }

    public static Integer getAuditCommitteeLoanTakenCount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory().values().stream()
                .filter(ex -> ex.getDescription() != null)
                .map(ex -> ex.getDescription().trim().toUpperCase())
                .filter(OfficeConstants.auditCommitteeMembers::contains)
                .collect(Collectors.toSet())
                .size();
    }

    public static Double getAuditCommitteeLoanAmount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory().values().stream()
                .filter(ex -> ex.getDescription() != null)
                .filter(ex -> OfficeConstants.auditCommitteeMembers
                        .contains(ex.getDescription().trim().toUpperCase()))
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
    }

    public static Double getAuditCommitteeLoanPercentage(ExcelLoanAgeingHelper loanHelper) {
        return (getAuditCommitteeLoanAmount(loanHelper) / loanHelper.getTotalBalance()) * 100;
    }

    public static Double getAuditCommitteeRecoveryRate(ExcelLoanAgeingHelper loanHelper) {
        Double goodPeopleTotalBalance = loanHelper.getFilteredPayCategory(
                        LoanPayerCategory.BELOW_ONE,
                        LoanPayerCategory.UPTO_ONE_MONTH,
                        LoanPayerCategory.ONE_TO_THREE_MONTHS
                )
                .values().stream()
                .filter(ex -> ex.getDescription() != null)
                .filter(ex -> OfficeConstants.auditCommitteeMembers
                        .contains(ex.getDescription().trim().toUpperCase()))
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
        Double total = getAuditCommitteeLoanAmount(loanHelper);
        return (goodPeopleTotalBalance / total) * 100;
    }

    public static String getAuditCommitteeQuality() {
        return "";
    }

    // Employees Calculation Methods
    public static Integer getEmployeesTotalCount() {
        return OfficeConstants.employeeMembers.size();
    }

    public static Integer getEmployeesLoanTakenCount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory().values().stream()
                .filter(ex -> ex.getDescription() != null)
                .map(ex -> ex.getDescription().trim().toUpperCase())
                .filter(OfficeConstants.employeeMembers::contains)
                .collect(Collectors.toSet())
                .size();
    }

    public static Double getEmployeesLoanAmount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory().values().stream()
                .filter(ex -> ex.getDescription() != null)
                .filter(ex -> OfficeConstants.employeeMembers
                        .contains(ex.getDescription().trim().toUpperCase()))
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
    }

    public static Double getEmployeesLoanPercentage(ExcelLoanAgeingHelper loanHelper) {
        return (getEmployeesLoanAmount(loanHelper) / loanHelper.getTotalBalance()) * 100;
    }

    public static Double getEmployeesRecoveryRate(ExcelLoanAgeingHelper loanHelper) {
        Double goodPeopleTotalBalance = loanHelper.getFilteredPayCategory(
                        LoanPayerCategory.BELOW_ONE,
                        LoanPayerCategory.UPTO_ONE_MONTH,
                        LoanPayerCategory.ONE_TO_THREE_MONTHS
                )
                .values().stream()
                .filter(ex -> ex.getDescription() != null)
                .filter(ex -> OfficeConstants.employeeMembers
                        .contains(ex.getDescription().trim().toUpperCase()))
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
        Double total = getEmployeesLoanAmount(loanHelper);
        return (goodPeopleTotalBalance / total) * 100;
    }

    public static String getEmployeesQuality() {
        return "";
    }

    public static void createDataSlide(XMLSlideShow ppt, String slideTitle,
                                       ExcelLoanAgeingHelper loanHelper) {
        XSLFSlide slide = ppt.createSlide();

        // Configure Nepali number formatter
        NumberFormat nepaliFormat = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepaliFormat.setMinimumFractionDigits(2);

        PPTUtils.makeTitleForSlide(slide, slideTitle);

        // Create table structure: 7 columns
        // 1 header row + 3 data rows = 4 rows
        int numRows = 4;
        int numCols = 7;

        XSLFTable table = slide.createTable(numRows, numCols);
        table.setAnchor(new Rectangle(10, 60, 700, 280));

        // Set column widths
        table.setColumnWidth(0, 150); // Description
        table.setColumnWidth(1, 80);  // Total Count
        table.setColumnWidth(2, 90);  // Loan Taken Count
        table.setColumnWidth(3, 100); // Loan Amount
        table.setColumnWidth(4, 100); // Percentage
        table.setColumnWidth(5, 90);  // Recovery Rate
        table.setColumnWidth(6, 90);  // Quality

        // Style for header cells (blue background)
        Color headerBlue = new Color(79, 129, 189);

        // Header row (row 0)
        String[] headers = {
                COLUMN_DESCRIPTION, COLUMN_TOTAL_COUNT, COLUMN_LOAN_TAKEN_COUNT,
                COLUMN_LOAN_AMOUNT, COLUMN_PERCENTAGE_TOTAL_LOAN,
                COLUMN_RECOVERY_RATE, COLUMN_QUALITY
        };

        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(0, col);
            PPTUtils.setCellTextWithStyle(cell,
                    headers[col], TextAlignment.CENTER, PresetColor.WHITE,
                    21.0, true,
                    headerBlue, NORMAL_BORDER_COLOR);
        }

        // Data rows (alternating white and light blue)
        Color white = Color.WHITE;
        Color lightBlue = new Color(217, 225, 242);

        // Row 1: Board Committee (संचालक समिति)
        fillDataRow(table, 1, ROW_BOARD_COMMITTEE,
                getBoardCommitteeTotalCount(),
                getBoardCommitteeLoanTakenCount(loanHelper),
                getBoardCommitteeLoanAmount(loanHelper),
                getBoardCommitteeLoanPercentage(loanHelper),
                getBoardCommitteeRecoveryRate(loanHelper),
                getBoardCommitteeQuality(),
                nepaliFormat, lightBlue);

        // Row 2: Audit Committee (लेखा सु. समिति)
        fillDataRow(table, 2, ROW_AUDIT_COMMITTEE,
                getAuditCommitteeTotalCount(),
                getAuditCommitteeLoanTakenCount(loanHelper),
                getAuditCommitteeLoanAmount(loanHelper),
                getAuditCommitteeLoanPercentage(loanHelper),
                getAuditCommitteeRecoveryRate(loanHelper),
                getAuditCommitteeQuality(),
                nepaliFormat, white);

        // Row 3: Employees (कर्मचारीहरु)
        fillDataRow(table, 3, ROW_EMPLOYEES,
                getEmployeesTotalCount(),
                getEmployeesLoanTakenCount(loanHelper),
                getEmployeesLoanAmount(loanHelper),
                getEmployeesLoanPercentage(loanHelper),
                getEmployeesRecoveryRate(loanHelper),
                getEmployeesQuality(),
                nepaliFormat, lightBlue);
    }

    // Helper method to fill a data row
    private static void fillDataRow(XSLFTable table, int row, String description,
                                    Integer totalCount, Integer loanTakenCount,
                                    Double loanAmount, Double percentage,
                                    Double recoveryRate, String quality,
                                    NumberFormat formatter, Color bgColor) {
        // Column 0: Description
        XSLFTableCell cell0 = table.getCell(row, 0);
        PPTUtils.setCellTextWithStyle(cell0,
                description, TextAlignment.LEFT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 1: Total Count
        XSLFTableCell cell1 = table.getCell(row, 1);
        String totalCountText = (totalCount != null) ? formatter.format(totalCount) : "";
        PPTUtils.setCellTextWithStyle(cell1,
                totalCountText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 2: Loan Taken Count
        XSLFTableCell cell2 = table.getCell(row, 2);
        String loanTakenText = (loanTakenCount != null) ? formatter.format(loanTakenCount) : "";
        PPTUtils.setCellTextWithStyle(cell2,
                loanTakenText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 3: Loan Amount
        XSLFTableCell cell3 = table.getCell(row, 3);
        String amountText = (loanAmount != null) ? formatter.format(loanAmount) : "";
        PPTUtils.setCellTextWithStyle(cell3,
                amountText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 4: Percentage
        XSLFTableCell cell4 = table.getCell(row, 4);
        String percentText = (percentage != null) ? formatter.format(percentage) : "";
        PPTUtils.setCellTextWithStyle(cell4,
                percentText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 5: Recovery Rate
        XSLFTableCell cell5 = table.getCell(row, 5);
        String recoveryText = (recoveryRate != null) ? formatter.format(recoveryRate) : "";
        PPTUtils.setCellTextWithStyle(cell5,
                recoveryText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 6: Quality
        XSLFTableCell cell6 = table.getCell(row, 6);
        String qualityText = (quality != null) ? quality : "";
        PPTUtils.setCellTextWithStyle(cell6,
                qualityText, TextAlignment.CENTER, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);
    }
}