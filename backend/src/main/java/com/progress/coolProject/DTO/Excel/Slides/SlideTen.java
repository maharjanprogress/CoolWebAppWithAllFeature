package com.progress.coolProject.DTO.Excel.Slides;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import com.progress.coolProject.Utils.Excel.ExcelTrialBalanceExcelRowHelper;
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

    private static final double NORMAL_FONT_SIZE = 11.0;
    private static final PresetColor NORMAL_FONT_COLOR = PresetColor.BLACK;
    private static final Color NORMAL_BORDER_COLOR = Color.BLACK;

    public static Double getTop30LoanAmount(ExcelTrialBalanceExcelRowHelper excel) {
        // TODO: Add calculation - Sum of top 30 borrowers' loan amounts
        return 0.0;
    }

    public static Double getTotalLoanInvestment(ExcelTrialBalanceExcelRowHelper excel) {
        // TODO: Add calculation - Total loan investment amount
        return 0.0;
    }

    public static Double getPercentageTop30(ExcelTrialBalanceExcelRowHelper excel) {
        // TODO: Add calculation - (top30LoanAmount / totalLoanInvestment) * 100
        return 0.0;
    }

    public static Double getTop30RecoveryRate(ExcelTrialBalanceExcelRowHelper excel) {
        // TODO: Add calculation - Recovery rate for top 30 borrowers
        return 0.0;
    }

    public static void createDataSlide(XMLSlideShow ppt, String slideTitle,
                                       ExcelTrialBalanceExcelRowHelper data) {
        XSLFSlide slide = ppt.createSlide();

        // Configure Nepali number formatter
        NumberFormat nepaliFormat = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepaliFormat.setMinimumFractionDigits(2);

        PPTUtils.makeTitleForSlide(slide, slideTitle);

        // Create table structure: 2 columns (Description | Amount)
        // 1 header row + 5 data rows = 6 rows
        int numRows = 6;
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
                "", TextAlignment.LEFT, PresetColor.WHITE,
                12.0, true,
                headerBlue, NORMAL_BORDER_COLOR);

        // Second column header
        XSLFTableCell headerCell1 = table.getCell(0, 1);
        PPTUtils.setCellTextWithStyle(headerCell1,
                COLUMN_AMOUNT, TextAlignment.CENTER, PresetColor.WHITE,
                12.0, true,
                headerBlue, NORMAL_BORDER_COLOR);

        // Data rows (alternating white and light blue)
        Color white = Color.WHITE;
        Color lightBlue = new Color(217, 225, 242);

        // Row 1: 30 जना ठूला ऋणीहरु (label only, no amount)
        fillDataRow(table, 1, ROW_1_TOP_30_BORROWERS, null,
                nepaliFormat, headerBlue, true);

        // Row 2: 30 जना ठूला ऋणीहरुले लिएको ऋण रकम रु
        fillDataRow(table, 2, ROW_2_TOP_30_LOAN_AMOUNT, getTop30LoanAmount(data),
                nepaliFormat, lightBlue, false);

        // Row 3: कूल ऋण लगानी रकम रु.
        fillDataRow(table, 3, ROW_3_TOTAL_LOAN_INVESTMENT, getTotalLoanInvestment(data),
                nepaliFormat, white, false);

        // Row 4: कूल ऋण लगानीमा ३० जना ऋणीले लिएको ऋणको प्रतिशत
        fillDataRow(table, 4, ROW_4_PERCENTAGE, getPercentageTop30(data),
                nepaliFormat, lightBlue, false);

        // Row 5: ३० जना ऋणीको ऋण असुली दर
        fillDataRow(table, 5, ROW_5_ACTUAL_INTEREST_RATE, getTop30RecoveryRate(data),
                nepaliFormat, white, false);
    }

    // Helper method to fill a data row
    private static void fillDataRow(XSLFTable table, int row, String description,
                                    Double amount, NumberFormat formatter,
                                    Color bgColor, boolean isBlueRow) {
        // Description column
        XSLFTableCell cell0 = table.getCell(row, 0);

        if (isBlueRow) {
            // Blue row with white text
            PPTUtils.setCellTextWithStyle(cell0,
                    description, TextAlignment.LEFT, PresetColor.WHITE,
                    NORMAL_FONT_SIZE, false,
                    bgColor, NORMAL_BORDER_COLOR);
        } else {
            // Normal row with black text
            PPTUtils.setCellTextWithStyle(cell0,
                    description, TextAlignment.LEFT, NORMAL_FONT_COLOR,
                    NORMAL_FONT_SIZE, false,
                    bgColor, NORMAL_BORDER_COLOR);
        }

        // Amount column
        XSLFTableCell cell1 = table.getCell(row, 1);
        String amountText = (amount != null) ? formatter.format(amount) : "";

        if (isBlueRow) {
            // Blue row with white text
            PPTUtils.setCellTextWithStyle(cell1,
                    amountText, TextAlignment.RIGHT, PresetColor.WHITE,
                    NORMAL_FONT_SIZE, false,
                    bgColor, NORMAL_BORDER_COLOR);
        } else {
            // Normal row with black text
            PPTUtils.setCellTextWithStyle(cell1,
                    amountText, TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                    NORMAL_FONT_SIZE, false,
                    bgColor, NORMAL_BORDER_COLOR);
        }
    }
}
