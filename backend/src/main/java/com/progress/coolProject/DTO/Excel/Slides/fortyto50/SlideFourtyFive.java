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
public class SlideFourtyFive {

    // Column headers
    private static final String COLUMN_INDICATOR = "संकेतहरु";
    private static final String COLUMN_EMPTY_1   = "";
    private static final String COLUMN_EMPTY_2   = "";
    private static final String COLUMN_MEASURE   = "हुनुपर्ने\n(लक्ष्य)";
    private static final String COLUMN_Q4        = "चौथो\nत्रैमासिक";
    private static final String COLUMN_Q3        = "तेस्रो\nत्रैमासिक";
    private static final String COLUMN_Q2        = "दोस्रो\nत्रैमासिक";
    private static final String COLUMN_Q1        = "पहिलो\nत्रैमासिक";

    // आर वान — ऋण लगानीबाट प्राप्त व्याज
    private static final String ROW_R1_IND  = "आर वान";
    private static final String ROW_R1_DET  = "ऋण\nलगानीबाट\nप्राप्त व्याज";
    private static final String ROW_R1_DESC = "ऋण लगानी गर्दा\nभएको सबै\nआम्दानीहरु";
    private static final String ROW_R1_TGT  = "ई एट\nपुग्ने गरी";

    // आर टु — तरल सम्पत्तिबाट प्राप्त व्याज
    private static final String ROW_R2_IND  = "आर टु";
    private static final String ROW_R2_DET  = "तरल\nसम्पत्तिबाट\nप्राप्त व्याज";
    private static final String ROW_R2_DESC = "बैंक खाताहरुबाट\nप्राप्त भएको\nव्याज रकम";
    private static final String ROW_R2_TGT  = "बजार दर";

    // आर थ्री — वित्तिय लगानीबाट प्राप्त आम्दानी
    private static final String ROW_R3_IND  = "आर थ्री";
    private static final String ROW_R3_DET  = "वित्तिय\nलगानीबाट\nप्राप्त आम्दानी";
    private static final String ROW_R3_DESC = "शेयर लाभांश, मुद्ति\nखाताको व्याज रकम";
    private static final String ROW_R3_TGT  = "बजार दर";

    private static final double      NORMAL_FONT_SIZE    = 14.0;
    private static final double      HEADER_FONT_SIZE    = 14.0;
    private static final PresetColor NORMAL_FONT_COLOR   = PresetColor.BLACK;
    private static final Color       NORMAL_BORDER_COLOR = Color.BLACK;

    public static void createDataSlide(XMLSlideShow ppt,
                                       String slideTitle,
                                       ExcelTrialBalanceExcelRowHelper plExcel,
                                       ExcelTrialBalanceExcelRowHelper bsExcel
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

        double r1Value = calcR1(plExcel,bsTotal);
        double r2Value = calcR2(plExcel, bsTotal);
        double r3Value = calcR3(plExcel, bsTotal);

        fillRow(table, 1, ROW_R1_IND, ROW_R1_DET, ROW_R1_DESC, ROW_R1_TGT,
                currentQuarter, r1Value, nepFmt, lightOrange);

        fillRow(table, 2, ROW_R2_IND, ROW_R2_DET, ROW_R2_DESC, ROW_R2_TGT,
                currentQuarter, r2Value, nepFmt, lightBlue);

        fillRow(table, 3, ROW_R3_IND, ROW_R3_DET, ROW_R3_DESC, ROW_R3_TGT,
                currentQuarter, r3Value, nepFmt, lightGreen);
    }

    // ── Calculations ──────────────────────────────────────────────────────────

    /**
     * R1: ऋण लगानीबाट प्राप्त व्याज
     * = Interest Income from Loans ÷ Total Loan Portfolio × 100
     * <p>
     * Numerator:   INTEREST_INCOME (3010) — all income earned from lending
     * Denominator: LOAN_ACCOUNT (3001)    — total loan portfolio balance
     */
    private static double calcR1(ExcelTrialBalanceExcelRowHelper plExcel, double totalLoan) {
        double interestIncome = plExcel.getCreditSum(TrialBalanceEnum.INTEREST_INCOME, TrialBalanceEnum.SERVICE_CHARGE);
        if (totalLoan == 0) return 0;
        return (interestIncome / totalLoan) * 100.0;
    }

    /**
     * R2: तरल सम्पत्तिबाट प्राप्त व्याज
     * = Bank Interest Income ÷ Total Bank Balances × 100
     * <p>
     * Numerator:   BANK_INTEREST_INCOME (9997)
     *            + KRISHI_BIKASH_SPECIAL_FD_INTEREST (3642)
     * Denominator: All bank/liquid balances
     */
    private static double calcR2(ExcelTrialBalanceExcelRowHelper plExcel, double bsTotal) {
        double bankInterestIncome = plExcel.getCredit(
                TrialBalanceEnum.BANK_INTEREST_INCOME
        );

        if (bsTotal == 0) return 0;
        return (bankInterestIncome / bsTotal) * 100.0;
    }

    /**
     * R3: वित्तिय लगानीबाट प्राप्त आम्दानी
     * = (Share Dividend + Fixed Deposit Interest) ÷ Total Financial Investments × 100
     * <p>
     * Numerator:   BANK_INTEREST_INCOME from dividend/FD sources
     *            + KRISHI_BIKASH_SPECIAL_FD_INTEREST (3642)
     *            + MISCELLANEOUS_INCOME (1023) — for share dividends if booked here
     * Denominator: Financial investment balances (shares + FDs)
     */
    private static double calcR3(ExcelTrialBalanceExcelRowHelper plExcel, double bsTotal) {
        double financialIncome = 0; //if dividend comes then only this will not be 0... but since the share owner doesn't get dividend it is 0

        if (bsTotal == 0) return 0;
        return (financialIncome / bsTotal) * 100.0;
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
