package com.progress.coolProject.DTO.Excel.Slides.fortyto50;

import com.progress.coolProject.Enums.TrialBalanceEnum;
import com.progress.coolProject.Enums.Traimasik;
import com.progress.coolProject.Utils.Excel.ExcelTrialBalanceExcelRowHelper;
import com.progress.coolProject.Utils.PowerPoint.PPTUtils;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import lombok.experimental.UtilityClass;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.text.TextAlignment;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;

import java.awt.*;

@UtilityClass
public class SlideFourtyEight {

    // Column headers
    private static final String COLUMN_INDICATOR = "संकेतहरु";
    private static final String COLUMN_EMPTY_1   = "";
    private static final String COLUMN_EMPTY_2   = "";
    private static final String COLUMN_MEASURE   = "हुनुपर्ने\n(लक्ष्य)";
    private static final String COLUMN_Q4        = "चौथो\nत्रैमासिक";
    private static final String COLUMN_Q3        = "तेस्रो\nत्रैमासिक";
    private static final String COLUMN_Q2        = "दोस्रो\nत्रैमासिक";
    private static final String COLUMN_Q1        = "पहिलो\nत्रैमासिक";

    // यल वान — तरलता
    private static final String ROW_L1_IND  = "यल वान";
    private static final String ROW_L1_DET  = "तरलता";
    private static final String ROW_L1_DESC = "नगद र बैंक\nखातामा रहेको रकम";
    private static final String ROW_L1_TGT  = "१०-२०%";

    // यल थ्री — नगद
    private static final String ROW_L3_IND  = "यल थ्री";
    private static final String ROW_L3_DET  = "नगद";
    private static final String ROW_L3_DESC = "संस्थाको नगद काउन्टर\nर भल्ट (ढुकुटि) मा\nरहेको रकम";
    private static final String ROW_L3_TGT  = "१% भन्द\nकम";

    private static final double      NORMAL_FONT_SIZE    = 14.0;
    private static final double      HEADER_FONT_SIZE    = 14.0;
    private static final PresetColor NORMAL_FONT_COLOR   = PresetColor.BLACK;
    private static final Color       NORMAL_BORDER_COLOR = Color.BLACK;

    public static void createDataSlide(XMLSlideShow ppt,
                                       String slideTitle,
                                       ExcelTrialBalanceExcelRowHelper excel) {

        XSLFSlide slide = ppt.createSlide();

        NumberFormat nepFmt = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepFmt.setMinimumFractionDigits(2);
        nepFmt.setMaximumFractionDigits(2);

        PPTUtils.makeTitleForSlide(slide, slideTitle);

        // 1 header row + 2 data rows, 8 columns
        XSLFTable table = slide.createTable(3, 8);
        table.setAnchor(new Rectangle(10, 60, 700, 350));

        table.setColumnWidth(0,  80);
        table.setColumnWidth(1, 100);
        table.setColumnWidth(2, 140);
        table.setColumnWidth(3,  76);
        table.setColumnWidth(4,  76);
        table.setColumnWidth(5,  76);
        table.setColumnWidth(6,  76);
        table.setColumnWidth(7,  76);

        Color headerBlue = new Color(79, 129, 189);

        // ── Header row ───────────────────────────────────────────────────────
        String[] headers = {
                COLUMN_INDICATOR, COLUMN_EMPTY_1, COLUMN_EMPTY_2, COLUMN_MEASURE,
                COLUMN_Q4, COLUMN_Q3, COLUMN_Q2, COLUMN_Q1
        };
        for (int col = 0; col < 8; col++) {
            XSLFTableCell cell = table.getCell(0, col);
            PPTUtils.setCellTextWithStyle(cell,
                    headers[col], TextAlignment.CENTER, PresetColor.WHITE,
                    HEADER_FONT_SIZE, true,
                    headerBlue, NORMAL_BORDER_COLOR);
        }

        Color lightOrange = new Color(255, 217, 194);
        Color lightBlue   = new Color(217, 225, 242);

        Traimasik currentQuarter = Traimasik.getCurrent();

        double l1Value = calcL1(excel);
        double l3Value = calcL3(excel);

        fillRow(table, 1, ROW_L1_IND, ROW_L1_DET, ROW_L1_DESC, ROW_L1_TGT,
                currentQuarter, l1Value, nepFmt, lightOrange);

        fillRow(table, 2, ROW_L3_IND, ROW_L3_DET, ROW_L3_DESC, ROW_L3_TGT,
                currentQuarter, l3Value, nepFmt, lightBlue);
    }

    // ── Calculations ──────────────────────────────────────────────────────────

    /**
     * L1: तरलता
     * = (Cash + All Bank Balances) ÷ Total Assets × 100
     *
     * Liquid assets = cash on hand + all bank/cooperative account balances
     */
    private static double calcL1(ExcelTrialBalanceExcelRowHelper excel) {
        double cash = excel.getDebit(TrialBalanceEnum.CASH);

        double bankBalances = excel.getDebitSum(
                TrialBalanceEnum.KRISHI_BIKASH_BANK,
                TrialBalanceEnum.NEPAL_INV_BANK,
                TrialBalanceEnum.NATIONAL_COOPERATIVE,
                TrialBalanceEnum.KASKUN_REGULAR_SAVING,
                TrialBalanceEnum.KASKUN_DAINIK,
                TrialBalanceEnum.KASKUN_TIME_SAVING,
                TrialBalanceEnum.BANK_DEVIDEND_SAVING,
                TrialBalanceEnum.SANIMA_BANK,
                TrialBalanceEnum.RSB_TIME_SAVING,
                TrialBalanceEnum.NEFSCUN_REGULAR_SAVING
        );

        double totalAssets = getTotalAssets(excel);
        if (totalAssets == 0) return 0;
        return ((cash + bankBalances) / totalAssets) * 100.0;
    }

    /**
     * L3: नगद
     * = Cash in vault/counter ÷ Total Assets × 100
     *
     * Only physical cash on hand (CASH ledger 5001),
     * excludes bank balances
     */
    private static double calcL3(ExcelTrialBalanceExcelRowHelper excel) {
        double cash        = excel.getDebit(TrialBalanceEnum.CASH);
        double totalAssets = getTotalAssets(excel);
        if (totalAssets == 0) return 0;
        return (cash / totalAssets) * 100.0;
    }

    /**
     * Total Assets — consistent with SlideFourtyFour/Seven.
     */
    private static double getTotalAssets(ExcelTrialBalanceExcelRowHelper excel) {
        double bankBalances = excel.getDebitSum(
                TrialBalanceEnum.KRISHI_BIKASH_BANK,
                TrialBalanceEnum.NEPAL_INV_BANK,
                TrialBalanceEnum.NATIONAL_COOPERATIVE,
                TrialBalanceEnum.KASKUN_REGULAR_SAVING,
                TrialBalanceEnum.KASKUN_DAINIK,
                TrialBalanceEnum.KASKUN_TIME_SAVING,
                TrialBalanceEnum.BANK_DEVIDEND_SAVING,
                TrialBalanceEnum.SANIMA_BANK,
                TrialBalanceEnum.RSB_TIME_SAVING,
                TrialBalanceEnum.SHARE_INVEST_NCBL
        );

        double loans = excel.getDebit(TrialBalanceEnum.LOAN_ACCOUNT);

        double fixedAssets = excel.getDebitSum(
                TrialBalanceEnum.LAND,
                TrialBalanceEnum.BUILDING,
                TrialBalanceEnum.FURNITURE,
                TrialBalanceEnum.PRINTER,
                TrialBalanceEnum.OFFICE_GOODS,
                TrialBalanceEnum.LAND_AND_BUILDING,
                TrialBalanceEnum.OFFICE_EQUIPMENTS
        );

        double cash = excel.getDebit(TrialBalanceEnum.CASH);

        double receivables = excel.getDebitSum(
                TrialBalanceEnum.ADVANCES_RECEIVABLES,
                TrialBalanceEnum.TDS_RECEIVABLES_ADVANCE_TAX,
                TrialBalanceEnum.TDS_ON_INTEREST_RECEIVABLE
        );

        double other = excel.getDebitSum(
                TrialBalanceEnum.SAMAN_MAUJDAT,
                TrialBalanceEnum.PUGIGAT_JAGAEDA_KOSH
        );

        return bankBalances + loans + fixedAssets + cash + receivables + other;
    }

    // ── Row / cell helpers ────────────────────────────────────────────────────

    /**
     * col 4 = Q4 (CHAUTHO, ordinal 3)
     * col 5 = Q3 (TYESRO,  ordinal 2)
     * col 6 = Q2 (DOSHRO,  ordinal 1)
     * col 7 = Q1 (PAHILO,  ordinal 0)
     */
    private static void fillRow(XSLFTable table, int row,
                                String indicator, String detail,
                                String description, String target,
                                Traimasik currentQuarter, double value,
                                NumberFormat fmt, Color bgColor) {

        fillCell(table, row, 0, indicator,   TextAlignment.LEFT,   bgColor);
        fillCell(table, row, 1, detail,      TextAlignment.LEFT,   bgColor);
        fillCell(table, row, 2, description, TextAlignment.LEFT,   bgColor);
        fillCell(table, row, 3, target,      TextAlignment.CENTER, bgColor);

        int[] ordinalByCol = {3, 2, 1, 0}; // Q4→Q1
        String formattedValue = fmt.format(value);

        for (int col = 4; col <= 7; col++) {
            boolean isCurrent = (currentQuarter.ordinal() == ordinalByCol[col - 4]);
            fillCell(table, row, col,
                    isCurrent ? formattedValue : "",
                    TextAlignment.CENTER, bgColor);
        }
    }

    private static void fillCell(XSLFTable table, int row, int col,
                                 String text, TextAlignment alignment, Color bgColor) {
        XSLFTableCell cell = table.getCell(row, col);
        PPTUtils.setCellTextWithStyle(cell,
                text, alignment, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);
    }
}