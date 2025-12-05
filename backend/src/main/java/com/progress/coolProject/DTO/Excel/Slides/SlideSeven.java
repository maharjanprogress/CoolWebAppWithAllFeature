package com.progress.coolProject.DTO.Excel.Slides;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import com.progress.coolProject.Enums.TrialBalanceEnum;
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
public final class SlideSeven {

    // Column Headers
    public static final String COLUMN_SERIAL = "क्.सं.";
    public static final String COLUMN_DESCRIPTION = "विवरण";
    public static final String COLUMN_AMOUNT = "रकम";

    // Row Labels
    public static final String ROW_1_SHARE_CAPITAL = "शेयर पुँजी";
    public static final String ROW_2_RESERVE_FUND = "जग्गेडाकोष";
    public static final String ROW_3_GHATA_PURTI_KOSH = "घाटा पूर्ति कोष";
    public static final String ROW_4_ACCUMULATED_PROFIT_LOSS = "संचित नाफा नोक्सानी";
    public static final String ROW_5_CAPITAL_GRANT = "पूँजीगत अनुदान";
    public static final String ROW_6_HOUSEHOLD_FUND = "घरजग्गा कोष";
    public static final String ROW_7_EXPENSES_NOT_ACCEPTED = "खर्च भएर नजाने कोष";
    public static final String ROW_TOTAL = "जम्मा प्राथमिक पूँजी";

    private static final double NORMAL_FONT_SIZE = 11.0;
    private static final PresetColor NORMAL_FONT_COLOR = PresetColor.BLACK;
    private static final Color NORMAL_BORDER_COLOR = Color.BLACK;

    // Calculation Methods
    public static Double getShareCapital(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(
                TrialBalanceEnum.SHARE_CAPITAL
        );
    }

    public static Double getReserveFund(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(
                TrialBalanceEnum.RESERVE_FUND
        );
    }

    public static Double getGhataPurtiKosh(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(
                TrialBalanceEnum.GHATA_PURTI_KOSH, TrialBalanceEnum.LOSS_FULFILMENT_FUND
        );
    }

    public static Double getAccumulatedProfitLoss(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(
                TrialBalanceEnum.LOAN_LOSS_PROVISION
        );
    }

    public static Double getCapitalGrant(ExcelTrialBalanceExcelRowHelper excel) {
        return 0.0;
    }

    public static Double getHouseholdFund(ExcelTrialBalanceExcelRowHelper excel) {
        return 0.0;
    }

    public static Double getExpensesNotAccepted(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(
                TrialBalanceEnum.JIBIKOPARCHAN_KOSH, TrialBalanceEnum.OTHER_RISK_MANAGEMENT_FUND,
                TrialBalanceEnum.SAMUDAIYIK_BIKASH_KOSH, TrialBalanceEnum.STHIRKARAN_KOSH,
                TrialBalanceEnum.DUBANTI_KOSH, TrialBalanceEnum.COOPERATIVE_DEVELOPMENT_FUND,
                TrialBalanceEnum.COOPERATIVE_PROMOTION_FUND, TrialBalanceEnum.PUGIGAT_JAGAEDA_KOSH
        );
    }

    public static Double getTotalPrimaryCapital(ExcelTrialBalanceExcelRowHelper excel) {
        return getShareCapital(excel) + getReserveFund(excel) + getGhataPurtiKosh(excel)
                + getAccumulatedProfitLoss(excel) + getCapitalGrant(excel)
                + getHouseholdFund(excel) + getExpensesNotAccepted(excel);
    }

    public static void createDataSlide(XMLSlideShow ppt, String slideTitle,
                                       ExcelTrialBalanceExcelRowHelper data) {
        XSLFSlide slide = ppt.createSlide();

        // Configure Nepali number formatter
        NumberFormat nepaliFormat = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepaliFormat.setMinimumFractionDigits(2);

        PPTUtils.makeTitleForSlide(slide, slideTitle);

        // Create table structure: 3 columns (Serial No, Description, Amount)
        // 1 header row + 7 data rows + 1 total row = 9 rows
        int numRows = 9;
        int numCols = 3;

        XSLFTable table = slide.createTable(numRows, numCols);
        table.setAnchor(new Rectangle(50, 60, 620, 380));

        // Set column widths
        table.setColumnWidth(0, 80);  // Serial Number
        table.setColumnWidth(1, 380); // Description
        table.setColumnWidth(2, 160); // Amount

        // Style for header cells (yellow background)
        Color headerYellow = new Color(255, 255, 0);

        // Header row (row 0)
        String[] headers = {COLUMN_SERIAL, COLUMN_DESCRIPTION, COLUMN_AMOUNT};

        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(0, col);
            PPTUtils.setCellTextWithStyle(cell,
                    headers[col], TextAlignment.CENTER, PresetColor.BLACK,
                    12.0, true,
                    headerYellow, NORMAL_BORDER_COLOR);
        }

        // Data rows (alternating white and light gray)
        Color white = Color.WHITE;
        Color lightGray = new Color(242, 242, 242);

        // Row 1: Share Capital
        fillDataRow(table, 1, "१", ROW_1_SHARE_CAPITAL, getShareCapital(data),
                nepaliFormat, white);

        // Row 2: Reserve Fund
        fillDataRow(table, 2, "२", ROW_2_RESERVE_FUND, getReserveFund(data),
                nepaliFormat, lightGray);

        // Row 3: Ghata Purti Kosh
        fillDataRow(table, 3, "३", ROW_3_GHATA_PURTI_KOSH, getGhataPurtiKosh(data),
                nepaliFormat, white);

        // Row 4: Accumulated Profit/Loss
        fillDataRow(table, 4, "४", ROW_4_ACCUMULATED_PROFIT_LOSS, getAccumulatedProfitLoss(data),
                nepaliFormat, lightGray);

        // Row 5: Capital Grant
        fillDataRow(table, 5, "५", ROW_5_CAPITAL_GRANT, getCapitalGrant(data),
                nepaliFormat, white);

        // Row 6: Household Fund
        fillDataRow(table, 6, "६", ROW_6_HOUSEHOLD_FUND, getHouseholdFund(data),
                nepaliFormat, lightGray);

        // Row 7: Expenses Not Accepted
        fillDataRow(table, 7, "७", ROW_7_EXPENSES_NOT_ACCEPTED, getExpensesNotAccepted(data),
                nepaliFormat, white);

        // Row 8: Total (no serial number, spans description column)
        Color totalWhite = Color.WHITE;

        // Empty serial number cell
        XSLFTableCell serialCell = table.getCell(8, 0);
        PPTUtils.setCellTextWithStyle(serialCell,
                "", TextAlignment.CENTER, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, true,
                totalWhite, NORMAL_BORDER_COLOR);

        // Total description
        XSLFTableCell descCell = table.getCell(8, 1);
        PPTUtils.setCellTextWithStyle(descCell,
                ROW_TOTAL, TextAlignment.LEFT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, true,
                totalWhite, NORMAL_BORDER_COLOR);

        // Total amount
        XSLFTableCell amountCell = table.getCell(8, 2);
        PPTUtils.setCellTextWithStyle(amountCell,
                nepaliFormat.format(getTotalPrimaryCapital(data)), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, true,
                totalWhite, NORMAL_BORDER_COLOR);
    }

    // Helper method to fill a data row
    private static void fillDataRow(XSLFTable table, int row, String serialNo,
                                    String description, Double amount,
                                    NumberFormat formatter, Color bgColor) {
        // Serial Number
        XSLFTableCell cell0 = table.getCell(row, 0);
        PPTUtils.setCellTextWithStyle(cell0,
                serialNo, TextAlignment.CENTER, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Description
        XSLFTableCell cell1 = table.getCell(row, 1);
        PPTUtils.setCellTextWithStyle(cell1,
                description, TextAlignment.LEFT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Amount
        XSLFTableCell cell2 = table.getCell(row, 2);
        PPTUtils.setCellTextWithStyle(cell2,
                formatter.format(amount), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);
    }
}
