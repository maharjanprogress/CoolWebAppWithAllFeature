package com.progress.coolProject.DTO.Excel.Slides;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
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
public final class SlideTen {
    // Row Labels
    public static final String ROW_1_TOP_30_BORROWERS = "३० जना ठूला ऋणीहरु";
    public static final String ROW_2_TOP_30_LOAN_AMOUNT = "३० जना ठूला ऋणीहरुले लिएको ऋण रकम रु";
    public static final String ROW_3_TOTAL_LOAN_INVESTMENT = "कूल ऋण लगानी रकम रु.";
    public static final String ROW_4_PERCENTAGE = "कूल ऋण लगानीमा ३० जना ऋणीले लिएको ऋणको प्रतिशत";
    public static final String ROW_5_ACTUAL_INTEREST_RATE = "३० जना ऋणीको ऋण असुली दर";

    // Column Header
    public static final String COLUMN_AMOUNT = "रकम रु";

    private static final double NORMAL_FONT_SIZE = 24.0;
    private static final PresetColor NORMAL_FONT_COLOR = PresetColor.BLACK;
    private static final Color NORMAL_BORDER_COLOR = Color.BLACK;

    public static Double getTop30LoanAmount(ExcelLoanAgeingHelper excel) {
        return excel.getTotalBalanceByCategory(30);
    }
    public static Double getTotalLoanInvestment(ExcelLoanAgeingHelper excel) {
        return excel.getTotalBalance();
    }

    public static Double getPercentageTop30(ExcelLoanAgeingHelper excel) {
        return (getTop30LoanAmount(excel) / getTotalLoanInvestment(excel)) * 100;
    }

    public static Double getTop30RecoveryRate(ExcelLoanAgeingHelper excel) {
        Double nicePeopleTotalBalance = excel.getTotalBalanceByCategory(30, LoanPayerCategory.BELOW_ONE, LoanPayerCategory.UPTO_ONE_MONTH, LoanPayerCategory.ONE_TO_THREE_MONTHS);
        Double total30LoanAmount = getTotalLoanInvestment(excel);
        return (nicePeopleTotalBalance / total30LoanAmount) * 100;
    }

    public static void createDataSlide(XMLSlideShow ppt, String slideTitle,
                                       ExcelLoanAgeingHelper data) {
        XSLFSlide slide = ppt.createSlide();

        // Configure Nepali number formatter
        NumberFormat nepaliFormat = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepaliFormat.setMinimumFractionDigits(2);

        PPTUtils.makeTitleForSlide(slide, slideTitle);

        // Create table structure: 2 columns (Description | Amount)
        // 1 header row + 5 data rows = 6 rows
        int numRows = 5;
        int numCols = 2;

        XSLFTable table = slide.createTable(numRows, numCols);
        table.setAnchor(new Rectangle(30, 60, 660, 380));

        // Set column widths
        table.setColumnWidth(0, 500); // Description column (wider)
        table.setColumnWidth(1, 160); // Amount column

        // Row 0: Header (blue background)
        Color headerBlue = new Color(79, 129, 189);

        // First column header is empty
        XSLFTableCell headerCell0 = table.getCell(0, 0);
        PPTUtils.setCellTextWithStyle(headerCell0,
                ROW_1_TOP_30_BORROWERS, TextAlignment.LEFT, PresetColor.WHITE,
                28.0, true,
                headerBlue, NORMAL_BORDER_COLOR);

        // Second column header
        XSLFTableCell headerCell1 = table.getCell(0, 1);
        PPTUtils.setCellTextWithStyle(headerCell1,
                COLUMN_AMOUNT, TextAlignment.CENTER, PresetColor.WHITE,
                28.0, true,
                headerBlue, NORMAL_BORDER_COLOR);

        // Data rows (alternating white and light blue)
        Color white = Color.WHITE;
        Color lightBlue = new Color(217, 225, 242);

        // Row 1: 30 जना ठूला ऋणीहरुले लिएको ऋण रकम रु
        fillDataRow(table, 1, ROW_2_TOP_30_LOAN_AMOUNT, getTop30LoanAmount(data),
                nepaliFormat, lightBlue);

        // Row 2: कूल ऋण लगानी रकम रु.
        fillDataRow(table, 2, ROW_3_TOTAL_LOAN_INVESTMENT, getTotalLoanInvestment(data),
                nepaliFormat, white);

        // Row 3: कूल ऋण लगानीमा ३० जना ऋणीले लिएको ऋणको प्रतिशत
        fillDataRow(table, 3, ROW_4_PERCENTAGE, getPercentageTop30(data),
                nepaliFormat, lightBlue);

        // Row 4: ३० जना ऋणीको ऋण असुली दर
        fillDataRow(table, 4, ROW_5_ACTUAL_INTEREST_RATE, getTop30RecoveryRate(data),
                nepaliFormat, white);
    }

    // Helper method to fill a data row
    private static void fillDataRow(XSLFTable table, int row, String description,
                                    Double amount, NumberFormat formatter,
                                    Color bgColor) {
        // Description column
        XSLFTableCell cell0 = table.getCell(row, 0);

        // Normal row with black text
        PPTUtils.setCellTextWithStyle(cell0,
                description, TextAlignment.LEFT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Amount column
        XSLFTableCell cell1 = table.getCell(row, 1);
        String amountText = (amount != null) ? formatter.format(amount) : "";
        if ((row == 3 || row == 4) && !amountText.isEmpty()){
            amountText = amountText + "%";
        }

        // Normal row with black text
        PPTUtils.setCellTextWithStyle(cell1,
                amountText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

    }
}
