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
public final class SlideThirteen {

    // Column Headers
    public static final String COLUMN_LOAN_PLAN = "ऋण योजनाहरुद";
    public static final String COLUMN_BORROWER_COUNT = "ऋणी संख्या";
    public static final String COLUMN_LOAN_AMOUNT = "ऋण रकम";
    public static final String COLUMN_AFFECTED_RISK_RATE = "भारित जोखिम दर";
    public static final String COLUMN_AFFECTED_RISK_AMOUNT = "भारित जोखिम रकम";
    public static final String COLUMN_INSTITUTIONAL_RISK_MANAGEMENT = "संस्थाले गरेको जोखिम व्यवस्था";
    public static final String COLUMN_REMAINING_RISK_AMOUNT = "नपुग/बढी जोखिम व्यवस्था रकम";

    // Row Labels
    public static final String ROW_BAD_LOAN = "खराब ऋण";
    public static final String ROW_DOUBTFUL_LOAN = "शंकास्पद ऋण";
    public static final String ROW_SUBSTANDARD_LOAN = "कमसल ऋण";
    public static final String ROW_WATCH_LIST_LOAN = "असल ऋण";
    public static final String ROW_TOTAL = "कूल जम्मा";

    private static final double NORMAL_FONT_SIZE = 17.0;
    private static final PresetColor NORMAL_FONT_COLOR = PresetColor.BLACK;
    private static final Color NORMAL_BORDER_COLOR = Color.BLACK;

    private static Double remainingLoanLossProvision;

    // Bad Loan (खराब ऋण) Calculation Methods
    public static Integer getBadLoanBorrowerCount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory(
                LoanPayerCategory.ONE_TO_TWO_YEARS,
                LoanPayerCategory.ABOVE_TWO_YEARS
        ).size();
    }

    public static Double getBadLoanAmount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory(
                LoanPayerCategory.ONE_TO_TWO_YEARS,
                LoanPayerCategory.ABOVE_TWO_YEARS
        )
                .values().stream()
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
    }

    public static String getBadLoanRiskRate() {
        return "१००%"; // Fixed: 100%
    }

    public static Double getBadLoanAffectedRiskAmount(ExcelLoanAgeingHelper loanHelper) {
        return getBadLoanAmount(loanHelper);
    }

    public static Double getBadLoanInstitutionalManagement(ExcelLoanAgeingHelper loanHelper) {
        Double compareNumber = getBadLoanAffectedRiskAmount(loanHelper);
        return compareWithRemainingBalance(compareNumber);
    }

    public static Double getBadLoanRemainingRisk(ExcelLoanAgeingHelper loanHelper) {
        remainingLoanLossProvision = remainingLoanLossProvision - getBadLoanAffectedRiskAmount(loanHelper);
        return remainingLoanLossProvision;
    }

    // Doubtful Loan (शंकास्पद ऋण) Calculation Methods
    public static Integer getDoubtfulLoanBorrowerCount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory(
                LoanPayerCategory.SIX_TO_NINE_MONTHS,
                LoanPayerCategory.NINE_TO_TWELVE_MONTHS
        ).size();
    }

    public static Double getDoubtfulLoanAmount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory(
                        LoanPayerCategory.SIX_TO_NINE_MONTHS,
                        LoanPayerCategory.NINE_TO_TWELVE_MONTHS
                )
                .values().stream()
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
    }

    public static String getDoubtfulLoanRiskRate() {
        return "५०%"; // Fixed: 50%
    }

    public static Double getDoubtfulLoanAffectedRiskAmount(ExcelLoanAgeingHelper loanHelper) {
        return 0.5 * getBadLoanAmount(loanHelper);
    }

    public static Double getDoubtfulLoanInstitutionalManagement(ExcelLoanAgeingHelper loanHelper) {
        Double compareNumber = getDoubtfulLoanAffectedRiskAmount(loanHelper);
        return compareWithRemainingBalance(compareNumber);
    }

    public static Double getDoubtfulLoanRemainingRisk(ExcelLoanAgeingHelper loanHelper) {
        remainingLoanLossProvision = remainingLoanLossProvision - getDoubtfulLoanAffectedRiskAmount(loanHelper);
        return  remainingLoanLossProvision;
    }

    // Substandard Loan (कमसल ऋण) Calculation Methods
    public static Integer getSubstandardLoanBorrowerCount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory(
                LoanPayerCategory.THREE_TO_SIX_MONTHS
        ).size();
    }

    public static Double getSubstandardLoanAmount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory(
                        LoanPayerCategory.THREE_TO_SIX_MONTHS
                )
                .values().stream()
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
    }

    public static String getSubstandardLoanRiskRate() {
        return "२५%"; // Fixed: 25%
    }

    public static Double getSubstandardLoanAffectedRiskAmount(ExcelLoanAgeingHelper loanHelper) {
        return 0.25 * getSubstandardLoanAmount(loanHelper);
    }

    public static Double getSubstandardLoanInstitutionalManagement(ExcelLoanAgeingHelper loanHelper) {
        Double compareNumber = getSubstandardLoanAffectedRiskAmount(loanHelper);
        return compareWithRemainingBalance(compareNumber);
    }

    public static Double getSubstandardLoanRemainingRisk(ExcelLoanAgeingHelper loanHelper) {
        remainingLoanLossProvision = remainingLoanLossProvision - getSubstandardLoanAffectedRiskAmount(loanHelper);
        return remainingLoanLossProvision;
    }

    // Watch List Loan (असल ऋण) Calculation Methods
    public static Integer getWatchListLoanBorrowerCount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory(
                LoanPayerCategory.BELOW_ONE,
                LoanPayerCategory.UPTO_ONE_MONTH,
                LoanPayerCategory.ONE_TO_THREE_MONTHS
        ).size();
    }

    public static Double getWatchListLoanAmount(ExcelLoanAgeingHelper loanHelper) {
        return loanHelper.getFilteredPayCategory(
                        LoanPayerCategory.BELOW_ONE,
                        LoanPayerCategory.UPTO_ONE_MONTH,
                        LoanPayerCategory.ONE_TO_THREE_MONTHS
                )
                .values().stream()
                .mapToDouble(LoanAccountAgeingDTO::getBalanceAmount)
                .sum();
    }

    public static String getWatchListLoanRiskRate() {
        return "१%"; // Fixed: 1%
    }

    public static Double getWatchListLoanAffectedRiskAmount(ExcelLoanAgeingHelper loanHelper) {
        return 0.01 * getWatchListLoanAmount(loanHelper);
    }

    public static Double getWatchListLoanInstitutionalManagement(ExcelLoanAgeingHelper loanHelper) {
        Double compareNumber = getWatchListLoanAffectedRiskAmount(loanHelper);
        return compareWithRemainingBalance(compareNumber);
    }

    public static Double getWatchListLoanRemainingRisk(ExcelLoanAgeingHelper loanHelper) {
        remainingLoanLossProvision = remainingLoanLossProvision - getWatchListLoanAffectedRiskAmount(loanHelper);
        return remainingLoanLossProvision;
    }

    // Total Calculation Methods
    public static Integer getTotalBorrowerCount(ExcelLoanAgeingHelper loanHelper) {
        return getBadLoanBorrowerCount(loanHelper) + getDoubtfulLoanBorrowerCount(loanHelper)
                + getSubstandardLoanBorrowerCount(loanHelper) + getWatchListLoanBorrowerCount(loanHelper);
    }

    public static Double getTotalLoanAmount(ExcelLoanAgeingHelper loanHelper) {
        return getBadLoanAmount(loanHelper) + getDoubtfulLoanAmount(loanHelper)
                + getSubstandardLoanAmount(loanHelper) + getWatchListLoanAmount(loanHelper);
    }

    public static String getTotalRiskRate() {
        return ""; // Empty for total row
    }

    public static Double getTotalAffectedRiskAmount(ExcelLoanAgeingHelper loanHelper) {
        return getBadLoanAffectedRiskAmount(loanHelper) + getDoubtfulLoanAffectedRiskAmount(loanHelper)
                + getSubstandardLoanAffectedRiskAmount(loanHelper) + getWatchListLoanAffectedRiskAmount(loanHelper);
    }

    public static void createDataSlide(XMLSlideShow ppt, String slideTitle,
                                       ExcelLoanAgeingHelper loanHelper,
                                       Double loanLossProvison
    ) {

        remainingLoanLossProvision = loanLossProvison;
        XSLFSlide slide = ppt.createSlide();

        // Configure Nepali number formatter
        NumberFormat nepaliFormat = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepaliFormat.setMinimumFractionDigits(2);

        PPTUtils.makeTitleForSlide(slide, slideTitle);

        // Create table structure: 7 columns
        // 1 header row + 4 data rows + 1 total row = 6 rows
        int numRows = 6;
        int numCols = 7;

        XSLFTable table = slide.createTable(numRows, numCols);
        table.setAnchor(new Rectangle(10, 60, 700, 380));

        // Set column widths
        table.setColumnWidth(0, 120); // Loan Plan
        table.setColumnWidth(1, 70);  // Borrower Count
        table.setColumnWidth(2, 100); // Loan Amount
        table.setColumnWidth(3, 80);  // Risk Rate
        table.setColumnWidth(4, 100); // Affected Risk Amount
        table.setColumnWidth(5, 110); // Institutional Management
        table.setColumnWidth(6, 120); // Remaining Risk

        // Style for header cells (blue background)
        Color headerBlue = new Color(79, 129, 189);

        // Header row (row 0)
        String[] headers = {
                COLUMN_LOAN_PLAN, COLUMN_BORROWER_COUNT, COLUMN_LOAN_AMOUNT,
                COLUMN_AFFECTED_RISK_RATE, COLUMN_AFFECTED_RISK_AMOUNT,
                COLUMN_INSTITUTIONAL_RISK_MANAGEMENT, COLUMN_REMAINING_RISK_AMOUNT
        };

        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(0, col);
            PPTUtils.setCellTextWithStyle(cell,
                    headers[col], TextAlignment.CENTER, PresetColor.WHITE,
                    18.0, true,
                    headerBlue, NORMAL_BORDER_COLOR);
        }

        // Data rows (alternating white and light blue)
        Color white = Color.WHITE;
        Color lightBlue = new Color(217, 225, 242);

        Double badLoanInstitutionalManagement = getBadLoanInstitutionalManagement(loanHelper);
        Double badLoanRemainingRisk = getBadLoanRemainingRisk(loanHelper);
        // Row 1: Bad Loan (खराब ऋण)
        fillDataRow(table, 1, ROW_BAD_LOAN,
                getBadLoanBorrowerCount(loanHelper),
                getBadLoanAmount(loanHelper),
                getBadLoanRiskRate(),
                getBadLoanAffectedRiskAmount(loanHelper),
                badLoanInstitutionalManagement,
                badLoanRemainingRisk,
                nepaliFormat, lightBlue);

        Double doubtfulInstutionalManagement = getDoubtfulLoanInstitutionalManagement(loanHelper);
        Double doubtfulLoanRemainingRisk = getDoubtfulLoanRemainingRisk(loanHelper);
        // Row 2: Doubtful Loan (शंकास्पद ऋण)
        fillDataRow(table, 2, ROW_DOUBTFUL_LOAN,
                getDoubtfulLoanBorrowerCount(loanHelper),
                getDoubtfulLoanAmount(loanHelper),
                getDoubtfulLoanRiskRate(),
                getDoubtfulLoanAffectedRiskAmount(loanHelper),
                doubtfulInstutionalManagement,
                doubtfulLoanRemainingRisk,
                nepaliFormat, white);

        Double substandardInstitutionalManagement = getSubstandardLoanInstitutionalManagement(loanHelper);
        Double substandardLoanRemainingRisk = getSubstandardLoanRemainingRisk(loanHelper);
        // Row 3: Substandard Loan (कमसल ऋण)
        fillDataRow(table, 3, ROW_SUBSTANDARD_LOAN,
                getSubstandardLoanBorrowerCount(loanHelper),
                getSubstandardLoanAmount(loanHelper),
                getSubstandardLoanRiskRate(),
                getSubstandardLoanAffectedRiskAmount(loanHelper),
                substandardInstitutionalManagement,
                substandardLoanRemainingRisk,
                nepaliFormat, lightBlue);

        Double watchListInstitutionalManagement = getWatchListLoanInstitutionalManagement(loanHelper);
        Double watchListLoanRemainingRisk = getWatchListLoanRemainingRisk(loanHelper);
        // Row 4: Watch List Loan (असल ऋण)
        fillDataRow(table, 4, ROW_WATCH_LIST_LOAN,
                getWatchListLoanBorrowerCount(loanHelper),
                getWatchListLoanAmount(loanHelper),
                getWatchListLoanRiskRate(),
                getWatchListLoanAffectedRiskAmount(loanHelper),
                watchListInstitutionalManagement,
                watchListLoanRemainingRisk,
                nepaliFormat, white);

        // Row 5: Total (कूल जम्मा)
        Double totalInstutionalManagement = badLoanInstitutionalManagement + doubtfulInstutionalManagement + substandardInstitutionalManagement + watchListInstitutionalManagement;
        Double totalRemainingRisk = badLoanRemainingRisk + doubtfulLoanRemainingRisk + substandardLoanRemainingRisk  + watchListLoanRemainingRisk;
        fillDataRow(table, 5, ROW_TOTAL,
                getTotalBorrowerCount(loanHelper),
                getTotalLoanAmount(loanHelper),
                getTotalRiskRate(),
                getTotalAffectedRiskAmount(loanHelper),
                totalInstutionalManagement,
                totalRemainingRisk,
                nepaliFormat, lightBlue);

        // Make total row bold
        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(5, col);
            String cellText = cell.getText();
            PPTUtils.setCellTextWithStyle(cell,
                    cellText, col == 0 ? TextAlignment.LEFT : (col == 3 ? TextAlignment.CENTER : TextAlignment.RIGHT),
                    NORMAL_FONT_COLOR, NORMAL_FONT_SIZE, true,
                    lightBlue, NORMAL_BORDER_COLOR);
        }
    }

    // Helper method to fill a data row
    private static void fillDataRow(XSLFTable table, int row, String loanPlan,
                                    Integer borrowerCount, Double loanAmount,
                                    String riskRate, Double affectedRiskAmount,
                                    Double institutionalManagement, Double remainingRisk,
                                    NumberFormat formatter, Color bgColor) {
        // Column 0: Loan Plan
        XSLFTableCell cell0 = table.getCell(row, 0);
        PPTUtils.setCellTextWithStyle(cell0,
                loanPlan, TextAlignment.LEFT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 1: Borrower Count
        XSLFTableCell cell1 = table.getCell(row, 1);
        String countText = (borrowerCount != null) ? formatter.format(borrowerCount) : "";
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

        // Column 3: Risk Rate
        XSLFTableCell cell3 = table.getCell(row, 3);
        PPTUtils.setCellTextWithStyle(cell3,
                riskRate, TextAlignment.CENTER, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 4: Affected Risk Amount
        XSLFTableCell cell4 = table.getCell(row, 4);
        String affectedText = (affectedRiskAmount != null) ? formatter.format(affectedRiskAmount) : "";
        PPTUtils.setCellTextWithStyle(cell4,
                affectedText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 5: Institutional Management
        XSLFTableCell cell5 = table.getCell(row, 5);
        String instText = (institutionalManagement != null) ? formatter.format(institutionalManagement) : "";
        PPTUtils.setCellTextWithStyle(cell5,
                instText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Column 6: Remaining Risk
        XSLFTableCell cell6 = table.getCell(row, 6);
        String remainingText = (remainingRisk != null) ? formatter.format(remainingRisk) : "";
        PPTUtils.setCellTextWithStyle(cell6,
                remainingText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);
    }

    private static Double compareWithRemainingBalance(Double amount){
        Double remainingBalance = remainingLoanLossProvision;
        if (amount >= remainingBalance) {
            if (remainingBalance <= 0.0) {
                return 0.0;
            }
            return remainingBalance;
        }
        return amount;
    }
}