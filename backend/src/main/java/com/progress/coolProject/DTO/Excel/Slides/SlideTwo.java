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
public final class SlideTwo {
    // Header
    public static final String FIRST_ROW_TITLE = NepaliDate.now().getYearInNepali() + " " + Traimasik.getmonthBetweenTraimasikofPlusmonth(-1) + " मसान्तसम्मको नाफा नोक्सान हिसाब विवरण";

    // Column Headers
    public static final String COLUMN_EXPENSE_DESCRIPTION = "खर्च डे.";
    public static final String COLUMN_AMOUNT = "रकम";
    public static final String COLUMN_INCOME_DESCRIPTION = "आम्दानी के.";
    public static final String COLUMN_INCOME_AMOUNT = "रकम";

    // Left Side Row Labels (Expenses)
    public static final String ROW_SAVINGS_INTEREST_EXPENSE = "बचतमा ब्याज खर्च";
    public static final String ROW_SEASONAL_INTEREST_EXPENSE = "ऋणमा ब्याज खर्च";
    public static final String ROW_EMPLOYEE_EXPENSE = "कर्मचारी खर्च";
    public static final String ROW_ADMINISTRATIVE_EXPENSE = "प्रशासनिक खर्च";

    // Right Side Row Labels (Income)
    public static final String ROW_BANK_INTEREST_INCOME = "बैंकबाट ब्याज आम्दानी";
    public static final String ROW_FINANCIAL_INVESTMENT_INCOME = "वित्तिय लगानीबाट आम्दानी";
    public static final String ROW_LOAN_INVESTMENT_INCOME = "ऋण लगानीबाट ब्याज आम्दानी";
    public static final String ROW_MISCELLANEOUS_INCOME = "विविध आम्दानी";

    private static final double NORMAL_FONT_SIZE = 11.0;
    private static final PresetColor NORMAL_FONT_COLOR = PresetColor.BLACK;
    private static final Color NORMAL_BORDER_COLOR = Color.BLACK;

    // Left Side Calculation Methods (Expenses)
    public static Double getSavingsInterestExpense(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getDebitSum(TrialBalanceEnum.INTEREST_EXPENDITURE, TrialBalanceEnum.INTEREST_EXPENDITURE_2);
    }

    public static Double getSeasonalInterestExpense(ExcelTrialBalanceExcelRowHelper excel) {
        return 0.0;
    }

    public static Double getEmployeeExpense(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getDebitSum(
                TrialBalanceEnum.SALARY, TrialBalanceEnum.PROVIDENT_EXP,
                TrialBalanceEnum.DASHAIN_ALLOWANCE
        );
    }

    public static Double getAdministrativeExpense(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getDebitSum(
                TrialBalanceEnum.OFFICE_EXPENSE, TrialBalanceEnum.AUDIT_FEE_EXPENSE,
                TrialBalanceEnum.TELEPHONE_EXPENSE, TrialBalanceEnum.ELECTRICITY_EXPENSE,
                TrialBalanceEnum.WATER_EXPENSE, TrialBalanceEnum.STATIONARY_EXPENSE,
                TrialBalanceEnum.PRINTING_EXPENSE, TrialBalanceEnum.TRAVELLING_EXPENSE,
                TrialBalanceEnum.TIFFIN_EXPENSE, TrialBalanceEnum.GENERAL_MEETING,
                TrialBalanceEnum.REPAIR_AND_MAINTENANCE_CHARGE, TrialBalanceEnum.AGM_EXPENSE,
                TrialBalanceEnum.TRAINING_CHARGE_EXPENSE, TrialBalanceEnum.MISCELLANEOUS_EXPENSE,
                TrialBalanceEnum.AMC_EXPENSE, TrialBalanceEnum.CLOUD_EXPENSE,
                TrialBalanceEnum.RAHAT_KHARCHA, TrialBalanceEnum.SANTONA_KHARCHA,
                TrialBalanceEnum.OFFICE_CLEANING, TrialBalanceEnum.VAT_EXP, TrialBalanceEnum.BONUS_EXPENCES
        );
    }

    // Right Side Calculation Methods (Income)
    public static Double getBankInterestIncome(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(
                TrialBalanceEnum.BANK_INTEREST_INCOME
        );
    }

    public static Double getFinancialInvestmentIncome(ExcelTrialBalanceExcelRowHelper excel) {
        return 0.0;
    }

    public static Double getLoanInvestmentIncome(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(
                TrialBalanceEnum.INTEREST_INCOME
        );
    }

    public static Double getMiscellaneousIncome(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(
                TrialBalanceEnum.MISCELLANEOUS_INCOME, TrialBalanceEnum.PASSBOOK_INCOME,
                TrialBalanceEnum.FINE_AND_PENALTY, TrialBalanceEnum.MEMBERSHIP_FEES_INCOME,
                TrialBalanceEnum.DISCOUNT_INCOME, TrialBalanceEnum.SERVICE_CHARGE
        );
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
        int numRows = 6; // 1 header + 4 data rows + 1 total row (no merged title needed here based on image)
        int numCols = 4;  // 2 columns for expenses + 2 columns for income

        XSLFTable table = slide.createTable(numRows, numCols);
        table.setAnchor(new Rectangle(20, 60, 680, 350));

        // Set column widths
        // Remember total slide width is 720, so 680 is considered here for table
        table.setColumnWidth(0, 210); // Expense Description
        table.setColumnWidth(1, 130);  // Expense Amount
        table.setColumnWidth(2, 210); // Income Description
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

        // Row 2: Savings Interest Expense | Bank Interest Income
        int row = 2;
        fillLeftSection(table, row, ROW_SAVINGS_INTEREST_EXPENSE, getSavingsInterestExpense(data),
                nepaliFormat, white);
        fillRightSection(table, row, ROW_BANK_INTEREST_INCOME, getBankInterestIncome(data),
                nepaliFormat, white);

        // Row 3: Seasonal Interest Expense | Financial Investment Income
        row = 3;
        fillLeftSection(table, row, ROW_SEASONAL_INTEREST_EXPENSE, getSeasonalInterestExpense(data),
                nepaliFormat, lightBlue);
        fillRightSection(table, row, ROW_FINANCIAL_INVESTMENT_INCOME, getFinancialInvestmentIncome(data),
                nepaliFormat, lightBlue);

        // Row 4: Employee Expense | Loan Investment Income
        row = 4;
        fillLeftSection(table, row, ROW_EMPLOYEE_EXPENSE, getEmployeeExpense(data),
                nepaliFormat, white);
        fillRightSection(table, row, ROW_LOAN_INVESTMENT_INCOME, getLoanInvestmentIncome(data),
                nepaliFormat, white);

        // Row 5: Administrative Expense | Miscellaneous Income
        row = 5;
        fillLeftSection(table, row, ROW_ADMINISTRATIVE_EXPENSE, getAdministrativeExpense(data),
                nepaliFormat, lightBlue);
        fillRightSection(table, row, ROW_MISCELLANEOUS_INCOME, getMiscellaneousIncome(data),
                nepaliFormat, lightBlue);

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
