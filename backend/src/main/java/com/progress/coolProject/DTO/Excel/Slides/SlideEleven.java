package com.progress.coolProject.DTO.Excel.Slides;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import com.progress.coolProject.DTO.Excel.LoanAccountAgeingDTO;
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

@UtilityClass
public final class SlideEleven {

    // Column Headers
    public static final String COLUMN_LOAN_PLAN = "धितोको आधारमा ऋण योजनाहरु";
    public static final String COLUMN_ACCOUNT_COUNT = "खाता संख्या";
    public static final String COLUMN_LOAN_AMOUNT = "ऋण रकम";
    public static final String COLUMN_LOAN_PERCENTAGE = "ऋण प्रतिशत";
    public static final String COLUMN_RECOVERY_RATE = "असुली दर";
    public static final String COLUMN_MEMBER_PERCENTAGE = "सदस्य सहभागिता प्रतिशत";

    // Row Labels
    public static final String ROW_SECURED_LOAN = "धितो जमानतमा ऋण";
    public static final String ROW_UNSECURED_LOAN = "विना धितो ऋण लगानी";
    public static final String ROW_ABOVE_3_LAKH_UNSECURED = "रु.३ लाखभन्दा बढी ऋण विना धितो";
    public static final String ROW_TOTAL = "कूल जम्मा";

    private static final double NORMAL_FONT_SIZE = 22.0;
    private static final PresetColor NORMAL_FONT_COLOR = PresetColor.BLACK;
    private static final Color NORMAL_BORDER_COLOR = Color.BLACK;

    // Calculation Methods
    public static Long getSecuredLoanCount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory().values().stream()
                .filter(pc -> pc.getBalanceAmount() > 100000)
                .count();
    }

    public static Double getSecuredLoanAmount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory().values().stream()
                .filter(pc -> pc.getBalanceAmount() > 100000)
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
    }

    public static Double getSecuredLoanPercentage(ExcelLoanAgeingHelper loanHelper) {
        return (getSecuredLoanAmount(loanHelper) / loanHelper.getTotalBalance()) * 100;
    }

    public static Double getSecuredLoanRecoveryRate(ExcelLoanAgeingHelper loanHelper) {
        Double goodPeoplesTotalBalance = loanHelper.getFilteredPayCategory(
                LoanPayerCategory.BELOW_ONE,
                        LoanPayerCategory.UPTO_ONE_MONTH,
                        LoanPayerCategory.ONE_TO_THREE_MONTHS
                )
                .values().stream()
                .filter(pc -> pc.getBalanceAmount() > 100000)
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
        Double total = getSecuredLoanAmount(loanHelper);
        return (goodPeoplesTotalBalance / total) * 100;
    }

    public static Double getSecuredLoanMemberPercentage(ExcelLoanAgeingHelper loanHelper) {
        return (double) ((getSecuredLoanCount(loanHelper) / getTotalLoanCount(loanHelper)) * 100);
    }

    public static Long getUnsecuredLoanCount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory().values().stream()
                .filter(pc -> pc.getBalanceAmount() <= 100000)
                .count();
    }

    public static Double getUnsecuredLoanAmount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory().values().stream()
                .filter(pc -> pc.getBalanceAmount() <= 100000)
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
    }

    public static Double getUnsecuredLoanPercentage(ExcelLoanAgeingHelper loanHelper) {
        return (getUnsecuredLoanAmount(loanHelper) / loanHelper.getTotalBalance()) * 100;
    }

    public static Double getUnsecuredLoanRecoveryRate(ExcelLoanAgeingHelper loanHelper) {
        Double goodPeoplesTotalBalance = loanHelper.getFilteredPayCategory(
                        LoanPayerCategory.BELOW_ONE,
                        LoanPayerCategory.UPTO_ONE_MONTH,
                        LoanPayerCategory.ONE_TO_THREE_MONTHS
                )
                .values().stream()
                .filter(pc -> pc.getBalanceAmount() <= 100000)
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
        Double total = getUnsecuredLoanAmount(loanHelper);
        return (goodPeoplesTotalBalance / total) * 100;
    }

    public static Double getUnsecuredLoanMemberPercentage(ExcelLoanAgeingHelper loanHelper) {
        return (double) ((getUnsecuredLoanCount(loanHelper) / getTotalLoanCount(loanHelper)) * 100);
    }

    public static Long getAbove3LakhUnsecuredCount(ExcelLoanAgeingHelper loanHelper) {
        return 0L;
    }

    public static Double getAbove3LakhUnsecuredAmount(ExcelLoanAgeingHelper loanHelper) {
        return 0.0;
    }

    public static Double getAbove3LakhUnsecuredPercentage(ExcelLoanAgeingHelper loanHelper) {
        return 0.0;
    }

    public static Double getAbove3LakhUnsecuredRecoveryRate(ExcelLoanAgeingHelper loanHelper) {
        return 0.0;
    }

    public static Long getTotalLoanCount(ExcelLoanAgeingHelper loanHelper) {
        return getSecuredLoanCount(loanHelper) + getUnsecuredLoanCount(loanHelper) + getAbove3LakhUnsecuredCount(loanHelper);
    }

    public static Double getTotalLoanAmount(ExcelLoanAgeingHelper loanHelper) {
        return getSecuredLoanAmount(loanHelper) + getUnsecuredLoanAmount(loanHelper) + getAbove3LakhUnsecuredAmount(loanHelper);
    }

    public static Double getTotalLoanPercentage(ExcelLoanAgeingHelper loanHelper) {
        return getSecuredLoanPercentage(loanHelper) + getUnsecuredLoanPercentage(loanHelper) + getAbove3LakhUnsecuredPercentage(loanHelper); // Always 100%
    }

    public static Double getTotalMemberPercentage(ExcelLoanAgeingHelper loanHelper) {
        return getSecuredLoanMemberPercentage(loanHelper) + getUnsecuredLoanMemberPercentage(loanHelper);
    }

    public static void createDataSlide(XMLSlideShow ppt, String slideTitle,
                                       ExcelLoanAgeingHelper loanHelper) {
        XSLFSlide slide = ppt.createSlide();

        // Configure Nepali number formatter
        NumberFormat nepaliFormat = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepaliFormat.setMinimumFractionDigits(2);

        PPTUtils.makeTitleForSlide(slide, slideTitle);

        // Create table structure: 6 columns
        // 1 header row + 3 data rows + 1 total row = 5 rows
        int numRows = 5;
        int numCols = 6;

        XSLFTable table = slide.createTable(numRows, numCols);
        table.setAnchor(new Rectangle(10, 60, 700, 350));

        // Set column widths
        table.setColumnWidth(0, 200); // Loan Plan
        table.setColumnWidth(1, 80);  // Account Count
        table.setColumnWidth(2, 120); // Loan Amount
        table.setColumnWidth(3, 100); // Loan Percentage
        table.setColumnWidth(4, 100); // Recovery Rate
        table.setColumnWidth(5, 100); // Member Percentage

        // Style for header cells (blue background)
        Color headerBlue = new Color(79, 129, 189);

        // Header row (row 0)
        String[] headers = {
                COLUMN_LOAN_PLAN, COLUMN_ACCOUNT_COUNT, COLUMN_LOAN_AMOUNT,
                COLUMN_LOAN_PERCENTAGE, COLUMN_RECOVERY_RATE, COLUMN_MEMBER_PERCENTAGE
        };

        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(0, col);
            PPTUtils.setCellTextWithStyle(cell,
                    headers[col], TextAlignment.CENTER, PresetColor.WHITE,
                    20.0, true,
                    headerBlue, NORMAL_BORDER_COLOR);
        }

        // Data rows (alternating white and light blue)
        Color white = Color.WHITE;
        Color lightBlue = new Color(217, 225, 242);

        // Row 1: Secured Loan (धितौं जमानतमा ऋण)
        fillDataRow(table, 1, ROW_SECURED_LOAN,
                getSecuredLoanCount(loanHelper),
                getSecuredLoanAmount(loanHelper),
                getSecuredLoanPercentage(loanHelper),
                getSecuredLoanRecoveryRate(loanHelper),
                getSecuredLoanMemberPercentage(loanHelper),
                nepaliFormat, white);

        // Row 2: Unsecured Loan (विना धितौं ऋण लगानी)
        fillDataRow(table, 2, ROW_UNSECURED_LOAN,
                getUnsecuredLoanCount(loanHelper),
                getUnsecuredLoanAmount(loanHelper),
                getUnsecuredLoanPercentage(loanHelper),
                getUnsecuredLoanRecoveryRate(loanHelper),
                getUnsecuredLoanMemberPercentage(loanHelper),
                nepaliFormat, lightBlue);

        // Row 3: Above 3 Lakh Unsecured (रु.३ लाखभन्दा बढी ऋण विना धितौं)
        fillDataRow(table, 3, ROW_ABOVE_3_LAKH_UNSECURED,
                getAbove3LakhUnsecuredCount(loanHelper),
                getAbove3LakhUnsecuredAmount(loanHelper),
                getAbove3LakhUnsecuredPercentage(loanHelper),
                getAbove3LakhUnsecuredRecoveryRate(loanHelper),
                null, // No member percentage for this row
                nepaliFormat, white);

        // Row 4: Total (कूल जम्मा) - white background
        fillDataRow(table, 4, ROW_TOTAL,
                getTotalLoanCount(loanHelper),
                getTotalLoanAmount(loanHelper),
                getTotalLoanPercentage(loanHelper),
                null, // No recovery rate in total row
                getTotalMemberPercentage(loanHelper),
                nepaliFormat, white);

        // Make total row bold
        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(4, col);
            // Re-apply styling with bold
            String cellText = cell.getText();
            PPTUtils.setCellTextWithStyle(cell,
                    cellText, col == 0 ? TextAlignment.LEFT : TextAlignment.RIGHT,
                    NORMAL_FONT_COLOR, NORMAL_FONT_SIZE, true,
                    white, NORMAL_BORDER_COLOR);
        }
    }

    // Helper method to fill a data row
    private static void fillDataRow(XSLFTable table, int row, String description,
                                    Long accountCount, Double loanAmount,
                                    Double loanPercentage, Double recoveryRate,
                                    Double memberPercentage,
                                    NumberFormat formatter, Color bgColor) {
        // Column 0: Description
        XSLFTableCell cell0 = table.getCell(row, 0);
        PPTUtils.setCellTextWithStyle(cell0,
                description, TextAlignment.LEFT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 1: Account Count
        XSLFTableCell cell1 = table.getCell(row, 1);
        String countText = (accountCount != null) ? formatter.format(accountCount) : "";
        PPTUtils.setCellTextWithStyle(cell1,
                countText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 2: Loan Amount
        XSLFTableCell cell2 = table.getCell(row, 2);
        String amountText = (loanAmount != null) ? formatter.format(loanAmount) : "";
        PPTUtils.setCellTextWithStyle(cell2,
                amountText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 3: Loan Percentage
        XSLFTableCell cell3 = table.getCell(row, 3);
        String percentText = (loanPercentage != null) ? formatter.format(loanPercentage) : "";
        if (!percentText.isEmpty()) {
            percentText = percentText + "%";
        }
        PPTUtils.setCellTextWithStyle(cell3,
                percentText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 4: Recovery Rate
        XSLFTableCell cell4 = table.getCell(row, 4);
        String recoveryText = (recoveryRate != null) ? formatter.format(recoveryRate) : "";
        if (!recoveryText.isEmpty()) {
            recoveryText = recoveryText + "%";
        }
        PPTUtils.setCellTextWithStyle(cell4,
                recoveryText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 5: Member Percentage
        XSLFTableCell cell5 = table.getCell(row, 5);
        String memberText = (memberPercentage != null) ? formatter.format(memberPercentage) : "";
        if (!memberText.isEmpty()) {
            memberText = memberText + "%";
        }
        PPTUtils.setCellTextWithStyle(cell5,
                memberText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);
    }
}