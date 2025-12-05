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
public final class SlideEight {
    // First Table Header
    public static final String FIRST_TABLE_TITLE = "६. कूल बचत निक्षेपमा प्राथमिक पूँजीकोषको प्रतिशत";

    // First Table Column Headers
    public static final String COLUMN_PRIMARY_CAPITAL = "प्राथमिक पूँजीकोष";
    public static final String COLUMN_SAVINGS_DEPOSIT = "बचत निक्षेप";
    public static final String COLUMN_TARGET = "लक्ष";
    public static final String COLUMN_AVAILABLE_NOW = "उपलब्धी हाल";
    public static final String COLUMN_AVAILABLE_PREVIOUS = "उपलब्धी गत";

    // Second Table Header
    public static final String SECOND_TABLE_TITLE = "७. कूल बचत निक्षेपमा नियमित अनिवार्य मासिक बचतको प्रतिशत";

    // Second Table Column Headers
    public static final String COLUMN_MANDATORY_MONTHLY_SAVINGS = "नियमित अनिवार्य मासिक बचत";

    private static final double NORMAL_FONT_SIZE = 18.0;
    private static final PresetColor NORMAL_FONT_COLOR = PresetColor.BLACK;
    private static final Color NORMAL_BORDER_COLOR = Color.BLACK;

    // First Table Calculation Methods
    public static Double getPrimaryCapital(ExcelTrialBalanceExcelRowHelper excel) {
        return SlideSeven.getTotalPrimaryCapital(excel);
    }

    public static Double getSavingsDepositFirstTable(ExcelTrialBalanceExcelRowHelper excel) {
        return SlideOne.getSavingsDeposit(excel);
    }

    public static String getTargetFirstTable() {
        return "१५%";
    }

    public static Double getAvailableNowFirstTable(ExcelTrialBalanceExcelRowHelper excel) {
        Double savingsDeposit = getSavingsDepositFirstTable(excel);
        if (savingsDeposit == null || savingsDeposit == 0.0) {
            return 0.0;
        }
        return (getPrimaryCapital(excel) / getSavingsDepositFirstTable(excel)) * 100;
    }

    public static Double getAvailablePreviousFirstTable(ExcelTrialBalanceExcelRowHelper excel) {
        return 0.0;
    }

    // Second Table Calculation Methods
    public static Double getMandatoryMonthlySavings(ExcelTrialBalanceExcelRowHelper excel) {
        //todo: there is this new excel file called saving summary
        return 33089854.6;
    }

    public static Double getSavingsDepositSecondTable(ExcelTrialBalanceExcelRowHelper excel) {
        return SlideOne.getSavingsDeposit(excel);
    }

    public static String getTargetSecondTable() {
        return "२५%";
    }

    public static Double getAvailableNowSecondTable(ExcelTrialBalanceExcelRowHelper excel) {
        Double savingsDeposit = getSavingsDepositSecondTable(excel);
        if (savingsDeposit == null || savingsDeposit == 0.0) {
            return 0.0;
        }
        return (getMandatoryMonthlySavings(excel) / getSavingsDepositFirstTable(excel)) * 100;
    }

    public static Double getAvailablePreviousSecondTable(ExcelTrialBalanceExcelRowHelper excel) {
        return 0.0;
    }

    public static void createDataSlide(XMLSlideShow ppt,
                                       ExcelTrialBalanceExcelRowHelper data) {
        XSLFSlide slide = ppt.createSlide();

        // Configure Nepali number formatter
        NumberFormat nepaliFormat = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepaliFormat.setMinimumFractionDigits(2);

        // Create First Table
        createFirstTable(slide, data, nepaliFormat);

        // Create Second Table
        createSecondTable(slide, data, nepaliFormat);
    }

    private static void createFirstTable(XSLFSlide slide, ExcelTrialBalanceExcelRowHelper data,
                                         NumberFormat nepaliFormat) {
        // First Table: 3 rows (1 title + 1 header + 1 data)
        int numRows = 3;
        int numCols = 5;

        XSLFTable table = slide.createTable(numRows, numCols);
        table.setAnchor(new Rectangle(30, 60, 660, 120));

        // Set column widths
        table.setColumnWidth(0, 160); // Primary Capital
        table.setColumnWidth(1, 160); // Savings Deposit
        table.setColumnWidth(2, 80);  // Target
        table.setColumnWidth(3, 130); // Available Now
        table.setColumnWidth(4, 130); // Available Previous

        // Row 0: Title (merged, yellow background)
        Color titleYellow = new Color(255, 223, 128);
        XSLFTableCell titleCell = table.getCell(0, 0);
        table.mergeCells(0, 0, 0, 4);
        PPTUtils.setCellTextWithStyle(titleCell,
                FIRST_TABLE_TITLE, TextAlignment.CENTER, PresetColor.BLACK,
                24.0, true,
                titleYellow, NORMAL_BORDER_COLOR);

        // Row 1: Header (light blue background)
        Color headerLightBlue = new Color(217, 225, 242);
        String[] headers = {
                COLUMN_PRIMARY_CAPITAL, COLUMN_SAVINGS_DEPOSIT, COLUMN_TARGET,
                COLUMN_AVAILABLE_NOW, COLUMN_AVAILABLE_PREVIOUS
        };

        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(1, col);
            PPTUtils.setCellTextWithStyle(cell,
                    headers[col], TextAlignment.CENTER, PresetColor.BLACK,
                    11.0, true,
                    headerLightBlue, NORMAL_BORDER_COLOR);
        }

        // Row 2: Data (white background)
        Color white = Color.WHITE;

        XSLFTableCell cell0 = table.getCell(2, 0);
        PPTUtils.setCellTextWithStyle(cell0,
                nepaliFormat.format(getPrimaryCapital(data)), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                white, NORMAL_BORDER_COLOR);

        XSLFTableCell cell1 = table.getCell(2, 1);
        PPTUtils.setCellTextWithStyle(cell1,
                nepaliFormat.format(getSavingsDepositFirstTable(data)), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                white, NORMAL_BORDER_COLOR);

        XSLFTableCell cell2 = table.getCell(2, 2);
        PPTUtils.setCellTextWithStyle(cell2,
                getTargetFirstTable(), TextAlignment.CENTER, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                white, NORMAL_BORDER_COLOR);

        XSLFTableCell cell3 = table.getCell(2, 3);
        PPTUtils.setCellTextWithStyle(cell3,
                nepaliFormat.format(getAvailableNowFirstTable(data)), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                white, NORMAL_BORDER_COLOR);

        XSLFTableCell cell4 = table.getCell(2, 4);
        PPTUtils.setCellTextWithStyle(cell4,
                nepaliFormat.format(getAvailablePreviousFirstTable(data)), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                white, NORMAL_BORDER_COLOR);
    }

    private static void createSecondTable(XSLFSlide slide, ExcelTrialBalanceExcelRowHelper data,
                                          NumberFormat nepaliFormat) {
        // Second Table: 3 rows (1 title + 1 header + 1 data)
        int numRows = 3;
        int numCols = 5;

        XSLFTable table = slide.createTable(numRows, numCols);
        table.setAnchor(new Rectangle(30, 220, 660, 120));

        // Set column widths (same as first table)
        table.setColumnWidth(0, 160); // Mandatory Monthly Savings
        table.setColumnWidth(1, 160); // Savings Deposit
        table.setColumnWidth(2, 80);  // Target
        table.setColumnWidth(3, 130); // Available Now
        table.setColumnWidth(4, 130); // Available Previous

        // Row 0: Title (merged, yellow background)
        Color titleYellow = new Color(255, 223, 128);
        XSLFTableCell titleCell = table.getCell(0, 0);
        table.mergeCells(0, 0, 0, 4);
        PPTUtils.setCellTextWithStyle(titleCell,
                SECOND_TABLE_TITLE, TextAlignment.CENTER, PresetColor.BLACK,
                24.0, true,
                titleYellow, NORMAL_BORDER_COLOR);

        // Row 1: Header (light blue background)
        Color headerLightBlue = new Color(217, 225, 242);
        String[] headers = {
                COLUMN_MANDATORY_MONTHLY_SAVINGS, COLUMN_SAVINGS_DEPOSIT, COLUMN_TARGET,
                COLUMN_AVAILABLE_NOW, COLUMN_AVAILABLE_PREVIOUS
        };

        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(1, col);
            PPTUtils.setCellTextWithStyle(cell,
                    headers[col], TextAlignment.CENTER, PresetColor.BLACK,
                    11.0, true,
                    headerLightBlue, NORMAL_BORDER_COLOR);
        }

        // Row 2: Data (white background)
        Color white = Color.WHITE;

        XSLFTableCell cell0 = table.getCell(2, 0);
        PPTUtils.setCellTextWithStyle(cell0,
                nepaliFormat.format(getMandatoryMonthlySavings(data)), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                white, NORMAL_BORDER_COLOR);

        XSLFTableCell cell1 = table.getCell(2, 1);
        PPTUtils.setCellTextWithStyle(cell1,
                nepaliFormat.format(getSavingsDepositSecondTable(data)), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                white, NORMAL_BORDER_COLOR);

        XSLFTableCell cell2 = table.getCell(2, 2);
        PPTUtils.setCellTextWithStyle(cell2,
                getTargetSecondTable(), TextAlignment.CENTER, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                white, NORMAL_BORDER_COLOR);

        XSLFTableCell cell3 = table.getCell(2, 3);
        PPTUtils.setCellTextWithStyle(cell3,
                nepaliFormat.format(getAvailableNowSecondTable(data)), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                white, NORMAL_BORDER_COLOR);

        XSLFTableCell cell4 = table.getCell(2, 4);
        PPTUtils.setCellTextWithStyle(cell4,
                nepaliFormat.format(getAvailablePreviousSecondTable(data)), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                white, NORMAL_BORDER_COLOR);
    }
}
