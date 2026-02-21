package com.progress.coolProject.DTO.Excel.Slides.fortyto50;

import com.progress.coolProject.Enums.LoanPayerCategory;
import com.progress.coolProject.Enums.TrialBalanceEnum;
import com.progress.coolProject.Enums.Traimasik;
import com.progress.coolProject.Utils.Excel.ExcelLoanAgeingHelper;
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
public class SlideFourtyFour {

    // Column headers (reversed quarters, same as 42 & 43)
    private static final String COLUMN_INDICATOR = "संकेतहरु";
    private static final String COLUMN_EMPTY_1   = "";
    private static final String COLUMN_EMPTY_2   = "";
    private static final String COLUMN_MEASURE   = "हुनुपर्ने (लक्ष्य)";
    private static final String COLUMN_Q4        = "चौथो त्रैमासिक";
    private static final String COLUMN_Q3        = "तेस्रो त्रैमासिक";
    private static final String COLUMN_Q2        = "दोस्रो त्रैमासिक";
    private static final String COLUMN_Q1        = "पहिलो त्रैमासिक";

    // ए वान — भाखा नाघेको ऋण रकम
    private static final String ROW_A1_IND  = "ए वान";
    private static final String ROW_A1_DET  = "भाखा नाघेको ऋण रकम";
    private static final String ROW_A1_DESC = "खराब, संकास्पद र कमसल ऋण रकम";
    private static final String ROW_A1_TGT  = "बढीमा ५%";

    // ए टु — नकमाउने सम्पत्ति
    private static final String ROW_A2_IND  = "ए टु";
    private static final String ROW_A2_DET  = "नकमाउने सम्पत्ति";
    private static final String ROW_A2_DESC = "नगद, पाउनुपर्ने हिसाब,  स्थिर सम्पत्ति, अन्य सम्पत्ति";
    private static final String ROW_A2_TGT  = "५% भन्दा कम";

    private static final double      NORMAL_FONT_SIZE    = 14.0;
    private static final double      HEADER_FONT_SIZE    = 14.0;
    private static final PresetColor NORMAL_FONT_COLOR   = PresetColor.BLACK;
    private static final Color       NORMAL_BORDER_COLOR = Color.BLACK;

    public static void createDataSlide(XMLSlideShow ppt,
                                       String slideTitle,
                                       ExcelLoanAgeingHelper loanHelper,
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

        double a1Value = calcA1(loanHelper, excel);
        double a2Value = calcA2(excel);

        fillRow(table, 1, ROW_A1_IND, ROW_A1_DET, ROW_A1_DESC, ROW_A1_TGT,
                currentQuarter, a1Value, nepFmt, lightOrange);

        fillRow(table, 2, ROW_A2_IND, ROW_A2_DET, ROW_A2_DESC, ROW_A2_TGT,
                currentQuarter, a2Value, nepFmt, lightBlue);
    }

    // ── Calculations ──────────────────────────────────────────────────────────

    /**
     * A1: भाखा नाघेको ऋण रकम
     * = (Substandard 3–6 months + Doubtful 6–12 months + Bad >12 months)
     *   ÷ Total Loan Portfolio × 100
     * <p>
     * Maps to LoanPayerCategory:
     *   Substandard  → THREE_TO_SIX_MONTHS
     *   Doubtful     → SIX_TO_NINE_MONTHS, NINE_TO_TWELVE_MONTHS
     *   Bad          → ONE_TO_TWO_YEARS, ABOVE_TWO_YEARS
     */
    private static double calcA1(ExcelLoanAgeingHelper loanHelper,
                                 ExcelTrialBalanceExcelRowHelper excel) {
        double nonPerformingLoans = loanHelper.getTotalBalanceByCategory(
                null,
                LoanPayerCategory.THREE_TO_SIX_MONTHS,
                LoanPayerCategory.SIX_TO_NINE_MONTHS,
                LoanPayerCategory.NINE_TO_TWELVE_MONTHS,
                LoanPayerCategory.ONE_TO_TWO_YEARS,
                LoanPayerCategory.ABOVE_TWO_YEARS
        );

        double totalLoan = loanHelper.getTotalBalance();
        if (totalLoan == 0) return 0;

        return (nonPerformingLoans / totalLoan) * 100.0;
    }

    /**
     * A2: नकमाउने सम्पत्ति
     * = (Cash + Receivables + Fixed Assets + Other Assets) ÷ Total Assets × 100
     * <p>
     * Non-earning assets include:
     *   Cash          → CASH
     *   Receivables   → ADVANCES_RECEIVABLES, TDS_RECEIVABLES_ADVANCE_TAX
     *   Fixed Assets  → LAND, BUILDING, FURNITURE, PRINTER, OFFICE_GOODS,
     *                   LAND_AND_BUILDING, OFFICE_EQUIPMENTS
     *   Other         → SAMAN_MAUJDAT
     */
    private static double calcA2(ExcelTrialBalanceExcelRowHelper excel) {
        double cash = excel.getDebit(TrialBalanceEnum.CASH);

        double receivables = excel.getDebitSum(
                TrialBalanceEnum.ADVANCES_RECEIVABLES,
                TrialBalanceEnum.TDS_RECEIVABLES_ADVANCE_TAX,
                TrialBalanceEnum.TDS_ON_INTEREST_RECEIVABLE
        );

        double fixedAssets = excel.getDebitSum(
                TrialBalanceEnum.LAND,
                TrialBalanceEnum.BUILDING,
                TrialBalanceEnum.FURNITURE,
                TrialBalanceEnum.PRINTER,
                TrialBalanceEnum.OFFICE_GOODS,
                TrialBalanceEnum.LAND_AND_BUILDING,
                TrialBalanceEnum.OFFICE_EQUIPMENTS
        );

        double otherAssets = excel.getDebit(TrialBalanceEnum.SAMAN_MAUJDAT);

        double nonEarningAssets = cash + receivables + fixedAssets + otherAssets;
        double totalAssets      = getTotalAssets(excel);

        if (totalAssets == 0) return 0;
        return (nonEarningAssets / totalAssets) * 100.0;
    }

    /**
     * Total Assets = sum of all debit-side balance sheet items.
     * Using bank balances + loan portfolio + fixed assets + cash + receivables.
     * Adjust if you have a dedicated total-assets ledger entry.
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

        int[] ordinalByCol = {3, 2, 1, 0}; // Q4, Q3, Q2, Q1
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
