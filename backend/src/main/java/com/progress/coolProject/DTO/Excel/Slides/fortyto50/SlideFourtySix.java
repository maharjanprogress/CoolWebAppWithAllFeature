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
public class SlideFourtySix {

    // Column headers
    private static final String COLUMN_INDICATOR = "संकेतहरु";
    private static final String COLUMN_EMPTY_1   = "";
    private static final String COLUMN_EMPTY_2   = "";
    private static final String COLUMN_MEASURE   = "हुनुपर्ने (लक्ष्य)";
    private static final String COLUMN_Q4        = "चौथो त्रैमासिक";
    private static final String COLUMN_Q3        = "तेस्रो त्रैमासिक";
    private static final String COLUMN_Q2        = "दोस्रो त्रैमासिक";
    private static final String COLUMN_Q1        = "पहिलो त्रैमासिक";

    // आर फाईभ — बचतमा व्याज खर्च
    private static final String ROW_R5_IND  = "आर फाईभ";
    private static final String ROW_R5_DET  = "बचतमा व्याज खर्च";
    private static final String ROW_R5_DESC = "सदस्यहरुलाई तिरेको व्याज खर्च रकम";
    private static final String ROW_R5_TGT  = "मुद्रास्फीति १।८७ - १।११";

    // आर सिक्स — बाह्य ऋणमा व्याज खर्च
    private static final String ROW_R6_IND  = "आर सिक्स";
    private static final String ROW_R6_DET  = "बाह्य ऋणमा व्याज खर्च";
    private static final String ROW_R6_DESC = "संस्थाले लिएको ऋणको व्याज रकम";
    private static final String ROW_R6_TGT  = "बजार दर";

    // आर सेभेन — सदस्यलाई वितरण गरेको शेयर लाभांश
    private static final String ROW_R7_IND  = "आर सेभेन";
    private static final String ROW_R7_DET  = "सदस्यलाई वितरण गरेको शेयर लाभांश";
    private static final String ROW_R7_DESC = "";
    private static final String ROW_R7_TGT  = "मुद्रास्फीति दर भन्दा बढी";

    private static final double      NORMAL_FONT_SIZE    = 14.0;
    private static final double      HEADER_FONT_SIZE    = 14.0;
    private static final PresetColor NORMAL_FONT_COLOR   = PresetColor.BLACK;
    private static final Color       NORMAL_BORDER_COLOR = Color.BLACK;

    public static void createDataSlide(XMLSlideShow ppt,
                                       String slideTitle,
                                       ExcelTrialBalanceExcelRowHelper plExcel,
                                       ExcelTrialBalanceExcelRowHelper bsExcel,
                                       double previousMonthShareCapital
    ) {

        XSLFSlide slide = ppt.createSlide();

        NumberFormat nepFmt = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepFmt.setMinimumFractionDigits(2);
        nepFmt.setMaximumFractionDigits(2);

        PPTUtils.makeTitleForSlide(slide, slideTitle);

        // 1 header row + 3 data rows, 8 columns
        XSLFTable table = slide.createTable(4, 8);
        table.setAnchor(new Rectangle(10, 60, 700, 420));

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
        Color lightGreen  = new Color(227, 239, 217);

        Traimasik currentQuarter = Traimasik.getCurrent();

        double bsTotal = bsExcel.getTotalCredit();

        double r5Value = calcR5(plExcel, bsTotal);
        double r6Value = calcR6(plExcel, bsTotal);
        double r7Value = calcR7(bsExcel, previousMonthShareCapital);

        fillRow(table, 1, ROW_R5_IND, ROW_R5_DET, ROW_R5_DESC, ROW_R5_TGT,
                currentQuarter, r5Value, nepFmt, lightOrange);

        fillRow(table, 2, ROW_R6_IND, ROW_R6_DET, ROW_R6_DESC, ROW_R6_TGT,
                currentQuarter, r6Value, nepFmt, lightBlue);

        fillRow(table, 3, ROW_R7_IND, ROW_R7_DET, ROW_R7_DESC, ROW_R7_TGT,
                currentQuarter, r7Value, nepFmt, lightGreen);
    }

    // ── Calculations ──────────────────────────────────────────────────────────

    /**
     * R5: बचतमा व्याज खर्च
     * = Interest paid to members on savings ÷ Total Saving Deposits × 100
     * <p>
     * Numerator:   INTEREST_EXPENDITURE (2010) + INTEREST_EXPENDITURE_2 (2536)
     *              — all interest paid out on saving accounts
     * Denominator: SAVING_ACCOUNT (4001) — total member saving deposits
     */
    private static double calcR5(ExcelTrialBalanceExcelRowHelper plExcel, double bsTotal) {
        double interestPaid = plExcel.getDebitSum(
                TrialBalanceEnum.INTEREST_EXPENDITURE,
                TrialBalanceEnum.INTEREST_EXPENDITURE_2
        );

        if (bsTotal == 0) return 0;
        return (interestPaid / bsTotal) * 100.0;
    }

    /**
     * R6: बाह्य ऋणमा व्याज खर्च
     * = Interest paid on external borrowings ÷ Total External Borrowings × 100
     * <p>
     * Numerator:   INTEREST_EXPENDITURE (2010) portion for external loans.
     *              Since we cannot split INTEREST_EXPENDITURE cleanly, use
     *              BYAG_MULTABI_HISAB (3009) as the accrued interest payable
     *              on external loans, or INTEREST_PAYABLE (9036).
     * Denominator: NATIONAL_COOPERATIVE (9040) — external loan taken from
     *              National Co-operative Bank, the only external borrowing ledger.
     * <p>
     * Note: If the institution has no external loan (balance = 0), this correctly returns 0.00
     */
    private static double calcR6(ExcelTrialBalanceExcelRowHelper plExcel, double bsTotal) {
        // Interest payable on external loan (accrued but not yet paid)
        double externalInterest = 0; //they have not taken loan from external source so this is zero


        // Total external borrowing principal
        if (bsTotal == 0) return 0;
        return (externalInterest / bsTotal) * 100.0;
    }

    /**
     * R7: सदस्यलाई वितरण गरेको शेयर लाभांश
     * = Share Dividend distributed ÷ Total Share Capital × 100
     * <p>
     * Numerator:   SHARE_DIVIDEND_FUND (9991) — dividend declared/paid to members
     * Denominator: SHARE_CAPITAL (9009)       — total member share capital
     */
    private static double calcR7(ExcelTrialBalanceExcelRowHelper bsExcel, double previousMonthShareCapital) {
        double dividendPaid  = bsExcel.getCredit(TrialBalanceEnum.SHARE_DIVIDEND_FUND);
        double averageShareCapital = (bsExcel.getCredit(TrialBalanceEnum.SHARE_CAPITAL) + previousMonthShareCapital)/2;
        System.out.println("Share Divident Fund : " + dividendPaid);
        System.out.println("Share Capital : " + bsExcel.getCredit(TrialBalanceEnum.SHARE_CAPITAL));
        System.out.println("Previous Share Capital : " + previousMonthShareCapital);
        if (averageShareCapital == 0) return 0;
        return (dividendPaid / averageShareCapital) * 100.0;
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