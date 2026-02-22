package com.progress.coolProject.DTO.Excel.Slides.fortyto50;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import com.progress.coolProject.DTO.Excel.Slides.SlideOne;
import com.progress.coolProject.DTO.Excel.Slides.SlideThirteen;
import com.progress.coolProject.Enums.Traimasik;
import com.progress.coolProject.Enums.TrialBalanceEnum;
import com.progress.coolProject.Utils.Excel.ExcelLoanAgeingHelper;
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
public class SlideFourtyTwo {

    // Column Headers — note: quarters are reversed (Q4 → Q1, left to right)
    public static final String COLUMN_INDICATOR  = "संकेतहरु";
    public static final String COLUMN_EMPTY_1    = "";
    public static final String COLUMN_EMPTY_2    = "";
    public static final String COLUMN_MEASURE    = "हुनुपर्ने (लक्ष्य)";
    public static final String COLUMN_Q4         = "चौथो त्रैमासिक";
    public static final String COLUMN_Q3         = "तेस्रो त्रैमासिक";
    public static final String COLUMN_Q2         = "दोस्रो त्रैमासिक";
    public static final String COLUMN_Q1         = "पहिलो त्रैमासिक";

    // Row labels  (matching image exactly)
    private static final String ROW_E1_IND   = "ई वान";
    private static final String ROW_E1_DET   = "खूद ऋण लगानी";
    private static final String ROW_E1_DESC  = "कूल ऋण-LLP";
    private static final String ROW_E1_TGT   = "७०%";

    private static final String ROW_E2_IND   = "ई टु";
    private static final String ROW_E2_DET   = "तरल सम्पत्तिको लगानी";
    private static final String ROW_E2_DESC  = "व्याज प्राप्त हुनेगरी बैंक खातामा रहेको रकम";
    private static final String ROW_E2_TGT   = "";          // no target in image

    private static final String ROW_E3_IND   = "ई थ्री";
    private static final String ROW_E3_DET   = "वित्तिय लगानी";
    private static final String ROW_E3_DESC  = "शेयर र मुद्तिमा रहेको रकम";
    private static final String ROW_E3_TGT   = "बढीमा १०%";

    private static final String ROW_E5_IND   = "ई फाईभ";
    private static final String ROW_E5_DET   = "बचत निक्षेप";
    private static final String ROW_E5_DESC  = "सदस्यहरुले जम्मा गरेको कूल बचत";
    private static final String ROW_E5_TGT   = "७०-८०%";

    private static final double  NORMAL_FONT_SIZE  = 14.0;
    private static final double  HEADER_FONT_SIZE  = 14.0;
    private static final PresetColor NORMAL_FONT_COLOR = PresetColor.BLACK;
    private static final Color   NORMAL_BORDER_COLOR   = Color.BLACK;

    public static void createDataSlide(XMLSlideShow ppt,
                                       String slideTitle,
                                       ExcelLoanAgeingHelper loanHelper,
                                       ExcelTrialBalanceExcelRowHelper bsExcel) {

        XSLFSlide slide = ppt.createSlide();

        // Nepali number formatter (2 decimal places for percentages)
        NumberFormat nepFmt = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepFmt.setMinimumFractionDigits(2);
        nepFmt.setMaximumFractionDigits(2);

        PPTUtils.makeTitleForSlide(slide, slideTitle);

        // 1 header row + 4 data rows, 8 columns
        int numRows = 5;
        int numCols = 8;
        XSLFTable table = slide.createTable(numRows, numCols);
        table.setAnchor(new Rectangle(10, 60, 700, 450));

        // Column widths  (same proportions as SlideFourtyOne)
        table.setColumnWidth(0,  80);   // संकेतहरु
        table.setColumnWidth(1, 100);   // detail label
        table.setColumnWidth(2, 140);   // description
        table.setColumnWidth(3,  76);   // target
        table.setColumnWidth(4,  76);   // Q4
        table.setColumnWidth(5,  76);   // Q3
        table.setColumnWidth(6,  76);   // Q2
        table.setColumnWidth(7,  76);   // Q1

        Color headerBlue = new Color(79, 129, 189);

        // ── Header row ──────────────────────────────────────────────────────
        String[] headers = {
                COLUMN_INDICATOR, COLUMN_EMPTY_1, COLUMN_EMPTY_2, COLUMN_MEASURE,
                COLUMN_Q4, COLUMN_Q3, COLUMN_Q2, COLUMN_Q1
        };
        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(0, col);
            PPTUtils.setCellTextWithStyle(cell,
                    headers[col], TextAlignment.CENTER, PresetColor.WHITE,
                    HEADER_FONT_SIZE, true,
                    headerBlue, NORMAL_BORDER_COLOR);
        }

        // ── Row colours (matching Protection slide palette) ──────────────
        Color lightOrange = new Color(255, 217, 194);
        Color lightBlue   = new Color(217, 225, 242);
        Color lightGreen  = new Color(227, 239, 217);
        Color lightGray   = new Color(242, 242, 242);

        // ── Determine current quarter ────────────────────────────────────
        Traimasik currentQuarter = Traimasik.getCurrent();

        // ── Calculate values for current quarter only ────────────────────
        //
        // E1: खूद ऋण लगानी  = (Total Loan - LLP) / Total Assets × 100
        double e1Value = calcE1(bsExcel, loanHelper);

        // E2: तरल सम्पत्ति  = Bank balances eligible for interest / Total Assets × 100
        double e2Value = calcE2(bsExcel);

        // E3: वित्तिय लगानी = Shares + Fixed deposits / Total Assets × 100
        double e3Value = calcE3(bsExcel);

        // E5: बचत निक्षेप   = Total Member Savings / Total Assets × 100
        double e5Value = calcE5(bsExcel);

        // ── Fill data rows ───────────────────────────────────────────────
        fillRow(table, 1, ROW_E1_IND, ROW_E1_DET, ROW_E1_DESC, ROW_E1_TGT,
                currentQuarter, e1Value, nepFmt, lightOrange);

        fillRow(table, 2, ROW_E2_IND, ROW_E2_DET, ROW_E2_DESC, ROW_E2_TGT,
                currentQuarter, e2Value, nepFmt, lightBlue);

        fillRow(table, 3, ROW_E3_IND, ROW_E3_DET, ROW_E3_DESC, ROW_E3_TGT,
                currentQuarter, e3Value, nepFmt, lightGreen);

        fillRow(table, 4, ROW_E5_IND, ROW_E5_DET, ROW_E5_DESC, ROW_E5_TGT,
                currentQuarter, e5Value, nepFmt, lightGray);
    }

    // ── Calculation helpers ──────────────────────────────────────────────────
    //    Adjust TrialBalanceEnum entries to whatever your enum actually exposes.

    private static double calcE1(ExcelTrialBalanceExcelRowHelper bsExcel, ExcelLoanAgeingHelper loanHelper) {
        // (Total Loan portfolio − Loan Loss Provision) ÷ Total Assets × 100

        double totalLoan = bsExcel.getDebit(TrialBalanceEnum.LOAN_ACCOUNT);          // adjust key
        double llp       = SlideThirteen.getTotalAffectedRiskAmount(loanHelper);
        double totalAssets = getTotalAssets(bsExcel);
        if (totalAssets == 0) return 0;
        return ((totalLoan - llp) / totalAssets) * 100.0;
    }

    private static double calcE2(ExcelTrialBalanceExcelRowHelper bsExcel) {
        // Bank / financial institution balances earning interest ÷ Total Assets × 100
        double bankBalances = SlideOne.getBank(bsExcel) + SlideOne.getCash(bsExcel);
        double totalAssets = getTotalAssets(bsExcel);
        if (totalAssets == 0) return 0;
        return (bankBalances / totalAssets) * 100.0;
    }

    private static double calcE3(ExcelTrialBalanceExcelRowHelper bsExcel) {
        // Shares + fixed deposits ÷ Total Assets × 100
        double financialInv = bsExcel.getDebitSum(TrialBalanceEnum.SHARE_INVEST_NCBL, TrialBalanceEnum.SHARE_JILLA_SAHAKARI, TrialBalanceEnum.SHARE_NEFSCUN); // adjust as needed
        double totalAssets = getTotalAssets(bsExcel);
        if (totalAssets == 0) return 0;
        return (financialInv / totalAssets) * 100.0;
    }

    private static double calcE5(ExcelTrialBalanceExcelRowHelper bsExcel) {
        // Total member savings ÷ Total Assets × 100
        double totalSavings = bsExcel.getCredit(TrialBalanceEnum.SAVING_ACCOUNT);
        double totalAssets = getTotalAssets(bsExcel);
        if (totalAssets == 0) return 0;
        return (totalSavings / totalAssets) * 100.0;
    }

    /** Shared total-assets calculation  */
    private static double getTotalAssets(ExcelTrialBalanceExcelRowHelper bsExcel) {
        return bsExcel.getTotalCredit();  // adjust key
    }

    // ── Row filler ───────────────────────────────────────────────────────────

    /**
     * Fill one data row.
     * Columns 4-7 map to Q4, Q3, Q2, Q1 (reversed).
     * Only the cell that corresponds to {@code currentQuarter} gets the value;
     * all others stay blank (as per spec).
     */
    private static void fillRow(XSLFTable table, int row,
                                String indicator, String detail, String description,
                                String target,
                                Traimasik currentQuarter, double value,
                                NumberFormat fmt, Color bgColor) {

        fillCell(table, row, 0, indicator,    TextAlignment.LEFT,   bgColor);
        fillCell(table, row, 1, detail,       TextAlignment.LEFT,   bgColor);
        fillCell(table, row, 2, description,  TextAlignment.LEFT,   bgColor);
        fillCell(table, row, 3, target,       TextAlignment.CENTER, bgColor);

        // Column → quarter mapping (reversed order)
        // col 4 = Q4 (CHAUTHO, ordinal 3)
        // col 5 = Q3 (TYESRO,  ordinal 2)
        // col 6 = Q2 (DOSHRO,  ordinal 1)
        // col 7 = Q1 (PAHILO,  ordinal 0)
        int[] ordinalByCol = {3, 2, 1, 0};  // indices into Traimasik.values()

        String formattedValue = fmt.format(value);

        for (int col = 4; col <= 7; col++) {
            int quarterOrdinal = ordinalByCol[col - 4];
            boolean isCurrent = (currentQuarter.ordinal() == quarterOrdinal);
            String cellText = isCurrent ? formattedValue : "";
            fillCell(table, row, col, cellText, TextAlignment.CENTER, bgColor);
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