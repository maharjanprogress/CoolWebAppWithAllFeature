package com.progress.coolProject.DTO.Excel.Slides;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import com.progress.coolProject.Enums.Traimasik;
import com.progress.coolProject.Enums.TrialBalanceEnum;
import com.progress.coolProject.Utils.Excel.ExcelTrialBalanceExcelRowHelper;
import com.progress.coolProject.Utils.PowerPoint.PPTUtils;
import com.progress.coolProject.Utils.date.NepaliDate;
import lombok.experimental.UtilityClass;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.text.TextAlignment;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;

import java.awt.*;

@UtilityClass
public final class SlideThree {
    // Header
    public static final String FIRST_ROW_TITLE = NepaliDate.now().plusMonths(-1).getYearInNepali() + " " + Traimasik.getmonthBetweenTraimasikofPlusmonth(-1) + " मसान्तसम्मको नाफा नोक्सान हिसाब विवरण";

    // Column Headers
    public static final String COLUMN_EXPENSE_DESCRIPTION = "खर्च डे.";
    public static final String COLUMN_AMOUNT = "रकम";
    public static final String COLUMN_INCOME_DESCRIPTION = "आम्दानी के.";
    public static final String COLUMN_INCOME_AMOUNT = "रकम";

    // Left Side Row Labels (Expenses/Debits)
    public static final String ROW_LOAN_LOSS_EXPENSE = "ऋण नोक्सानी खर्च";
    public static final String ROW_TOTAL_EXPENSE_K = "जम्मा खर्च (क)";
    public static final String ROW_TOTAL_PROFIT_KH = "जम्मा नाफा (ख)";
    public static final String ROW_TOTAL_SUM_K_KH = "जम्मा रु (क+ख)";

    // Right Side Row Labels (Income/Credits)
    public static final String ROW_ACCOUNT_CLOSED = "खाता बन्द";
    public static final String ROW_TOTAL_INCOME_G = "जम्मा आम्दानी (ग)";
    public static final String ROW_TOTAL_LOSS_GH = "जम्मा नोक्सान (घ)";
    public static final String ROW_TOTAL_SUM_G_GH = "जम्मा रु. (ग+घ)";

    private static final double NORMAL_FONT_SIZE = 11.0;
    private static final PresetColor NORMAL_FONT_COLOR = PresetColor.BLACK;
    private static final Color NORMAL_BORDER_COLOR = Color.BLACK;

    // Left Side Calculation Methods (Expenses/Debits)
    public static Double getLoanLossExpense(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getDebitSum(
                TrialBalanceEnum.LOAN_LOSS_PROVISION_EXPENSES
        );
    }

    public static Double getTotalExpenseK(ExcelTrialBalanceExcelRowHelper excel) {
        return getLoanLossExpense(excel) +
                SlideTwo.getSavingsInterestExpense(excel) +
                SlideTwo.getSeasonalInterestExpense(excel) +
                SlideTwo.getEmployeeExpense(excel) +
                SlideTwo.getAdministrativeExpense(excel);
    }

    public static Double getTotalProfitKh(ExcelTrialBalanceExcelRowHelper excel) {
        double totalIncomeG = getTotalIncomeG(excel);
        double totalExpenseK = getTotalExpenseK(excel);
        double profitKh = totalIncomeG - totalExpenseK;
        return profitKh > 0 ? profitKh : 0.0;
    }

    public static Double getTotalSumKKh(ExcelTrialBalanceExcelRowHelper excel) {
        return getTotalExpenseK(excel) + getTotalProfitKh(excel);
    }

    // Right Side Calculation Methods (Income/Credits)
    public static Double getAccountClosed(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(
                TrialBalanceEnum.ACCOUNT_CLOSING_CHARGE
        );
    }

    public static Double getTotalIncomeG(ExcelTrialBalanceExcelRowHelper excel) {
        return getAccountClosed(excel) +
                SlideTwo.getBankInterestIncome(excel) +
                SlideTwo.getFinancialInvestmentIncome(excel) +
                SlideTwo.getLoanInvestmentIncome(excel)+
                SlideTwo.getMiscellaneousIncome(excel);
    }

    public static Double getTotalLossGh(ExcelTrialBalanceExcelRowHelper excel) {
        double totalIncomeG = getTotalIncomeG(excel);
        double totalExpenseK = getTotalExpenseK(excel);
        double lossGh = totalExpenseK - totalIncomeG;
        return lossGh > 0 ? lossGh : 0.0;
    }

    public static Double getTotalSumGGh(ExcelTrialBalanceExcelRowHelper excel) {
        return getTotalIncomeG(excel) + getTotalLossGh(excel);
    }

    public static void createDataSlide(XMLSlideShow ppt, String slideTitle,
                                       ExcelTrialBalanceExcelRowHelper data) {
        XSLFSlide slide = ppt.createSlide();

        // Configure Nepali number formatter
        NumberFormat nepaliFormat = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepaliFormat.setMinimumFractionDigits(2);

        PPTUtils.makeTitleForSlide(slide, slideTitle);

        // Create table structure: 2 main sections (left and right)
        // Left: Expense Description | Amount
        // Right: Income Description | Amount
        int numRows = 6; // 1 title row + 1 header + 4 data rows
        int numCols = 4; // 2 columns for expenses + 2 columns for income

        XSLFTable table = slide.createTable(numRows, numCols);
        table.setAnchor(new Rectangle(20, 60, 680, 350));

        // Set column widths
        table.setColumnWidth(0, 210);     // Expense Description
        table.setColumnWidth(1, 130);    // Expense Amount
        table.setColumnWidth(2, 210);   // Income Description
        table.setColumnWidth(3, 130);  // Income Amount

        // ROW 0: Merged title row (merge all 4 cells)
        Color headerBlue = new Color(79, 129, 189);
        XSLFTableCell mergedCell = table.getCell(0, 0);

        table.mergeCells(0, 0, 0, 3);
        PPTUtils.setCellTextWithStyle(mergedCell,
                FIRST_ROW_TITLE, TextAlignment.CENTER, PresetColor.BLACK,
                14.0, true,
                headerBlue, NORMAL_BORDER_COLOR);

        // Style for header cells (yellow background)
        Color headerYellow = new Color(255, 255, 0);

        // Header row (row 1)
        String[] headers = {
                COLUMN_EXPENSE_DESCRIPTION, COLUMN_AMOUNT,
                COLUMN_INCOME_DESCRIPTION, COLUMN_INCOME_AMOUNT
        };

        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(1, col);
            PPTUtils.setCellTextWithStyle(cell,
                    headers[col], TextAlignment.CENTER, PresetColor.BLACK,
                    12.0, true,
                    headerYellow, NORMAL_BORDER_COLOR);
        }

        // Data rows (alternating white and light blue)
        Color lightBlue = new Color(217, 225, 242);
        Color white = Color.WHITE;

        // Row 2: Loan Loss Expense | Account Closed
        int row = 2;
        fillLeftSection(table, row, ROW_LOAN_LOSS_EXPENSE, getLoanLossExpense(data),
                nepaliFormat, lightBlue);
        fillRightSection(table, row, ROW_ACCOUNT_CLOSED, getAccountClosed(data),
                nepaliFormat, lightBlue);

        // Row 3: Total Expense (क) | Total Income (ग)
        row = 3;
        fillLeftSection(table, row, ROW_TOTAL_EXPENSE_K, getTotalExpenseK(data),
                nepaliFormat, white);
        fillRightSection(table, row, ROW_TOTAL_INCOME_G, getTotalIncomeG(data),
                nepaliFormat, white);

        // Row 4: Total Profit (ख) | Total Loss (घ)
        row = 4;
        fillLeftSection(table, row, ROW_TOTAL_PROFIT_KH, getTotalProfitKh(data),
                nepaliFormat, lightBlue);
        fillRightSection(table, row, ROW_TOTAL_LOSS_GH, getTotalLossGh(data),
                nepaliFormat, lightBlue);

        // Row 5: Total Sum (क+ख) | Total Sum (ग+घ) - These should match
        row = 5;
        fillLeftSection(table, row, ROW_TOTAL_SUM_K_KH, getTotalSumKKh(data),
                nepaliFormat, white);
        fillRightSection(table, row, ROW_TOTAL_SUM_G_GH, getTotalSumGGh(data),
                nepaliFormat, white);
    }

    // Helper method to fill left section (Expenses)
    private static void fillLeftSection(XSLFTable table, int row, String description,
                                        Double amount, NumberFormat formatter, Color bgColor) {
        // Description
        XSLFTableCell cell0 = table.getCell(row, 0);
        PPTUtils.setCellTextWithStyle(cell0,
                description, TextAlignment.LEFT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Amount
        XSLFTableCell cell1 = table.getCell(row, 1);
        PPTUtils.setCellTextWithStyle(cell1,
                formatter.format(amount), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);
    }

    // Helper method to fill right section (Income)
    private static void fillRightSection(XSLFTable table, int row, String description,
                                         Double amount, NumberFormat formatter, Color bgColor) {
        // Description
        XSLFTableCell cell2 = table.getCell(row, 2);
        PPTUtils.setCellTextWithStyle(cell2,
                description, TextAlignment.LEFT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Amount
        XSLFTableCell cell3 = table.getCell(row, 3);
        PPTUtils.setCellTextWithStyle(cell3,
                formatter.format(amount), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);
    }
}