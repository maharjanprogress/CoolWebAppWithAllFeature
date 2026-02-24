package com.progress.coolProject.DTO.Excel.Slides.fortyto50;

import lombok.experimental.UtilityClass;
import com.progress.coolProject.Enums.TrialBalanceEnum;
import com.progress.coolProject.Enums.Traimasik;
import com.progress.coolProject.Utils.Excel.ExcelTrialBalanceExcelRowHelper;
import com.progress.coolProject.Utils.PowerPoint.PPTUtils;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.text.TextAlignment;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTable;
import org.apache.poi.xslf.usermodel.XSLFTableCell;

import java.awt.*;

@UtilityClass
public class SlideFourtyThree {

    // Column headers
    private static final String COLUMN_INDICATOR = "संकेतहरु";
    private static final String COLUMN_EMPTY_1   = "";
    private static final String COLUMN_EMPTY_2   = "";
    private static final String COLUMN_MEASURE   = "हुनुपर्ने (लक्ष्य)";
    private static final String COLUMN_Q4        = "चौथो त्रैमासिक";
    private static final String COLUMN_Q3        = "तेस्रो त्रैमासिक";
    private static final String COLUMN_Q2        = "दोस्रो त्रैमासिक";
    private static final String COLUMN_Q1        = "पहिलो त्रैमासिक";

    // Row: ई सिक्स — बाह्य ऋण
    private static final String ROW_E6_IND  = "ई सिक्स";
    private static final String ROW_E6_DET  = "बाह्य ऋण";
    private static final String ROW_E6_DESC = "संस्थाले लिएको ऋण";
    private static final String ROW_E6_TGT  = "बढीमा ५%";

    // Row: ई सेभेन — शेयर पुँजी
    private static final String ROW_E7_IND  = "ई सेभेन";
    private static final String ROW_E7_DET  = "शेयर पुँजी";
    private static final String ROW_E7_DESC = "सदस्यहरुले खरिद गरेको शेयर रकम";
    private static final String ROW_E7_TGT  = "१०-२०%";

    // Row: ई एट — संस्थागत पुँजी
    private static final String ROW_E8_IND  = "ई एट";
    private static final String ROW_E8_DET  = "संस्थागत पुँजी";
    private static final String ROW_E8_DESC = "जगेडाकोष, घाटा पूर्तिकोष, घरजग्गाकोष, पूँजीगत अनुदान, खर्च भएर नजाने कोषहरु";
    private static final String ROW_E8_TGT  = "कम्तिमा १०%";

    private static final double      NORMAL_FONT_SIZE   = 14.0;
    private static final double      HEADER_FONT_SIZE   = 14.0;
    private static final PresetColor NORMAL_FONT_COLOR  = PresetColor.BLACK;
    private static final Color       NORMAL_BORDER_COLOR = Color.BLACK;

    public static void createDataSlide(XMLSlideShow ppt,
                                       String slideTitle,
                                       ExcelTrialBalanceExcelRowHelper bsExcel) {

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

        // ── Row colours ───────────────────────────────────────────────────────
        Color lightOrange = new Color(255, 217, 194);
        Color lightBlue   = new Color(217, 225, 242);
        Color lightGreen  = new Color(227, 239, 217);

        // ── Current quarter only ──────────────────────────────────────────────
        Traimasik currentQuarter = Traimasik.getCurrent();

        double e6Value = calcE6(bsExcel);
        double e7Value = calcE7(bsExcel);
        double e8Value = calcE8(bsExcel);

        fillRow(table, 1, ROW_E6_IND, ROW_E6_DET, ROW_E6_DESC, ROW_E6_TGT,
                currentQuarter, e6Value, nepFmt, lightOrange);

        fillRow(table, 2, ROW_E7_IND, ROW_E7_DET, ROW_E7_DESC, ROW_E7_TGT,
                currentQuarter, e7Value, nepFmt, lightBlue);

        fillRow(table, 3, ROW_E8_IND, ROW_E8_DET, ROW_E8_DESC, ROW_E8_TGT,
                currentQuarter, e8Value, nepFmt, lightGreen);
    }

    // ── Calculations ──────────────────────────────────────────────────────────

    /**
     * E6: बाह्य ऋण — External borrowings taken by the institution ÷ Total Assets × 100
     */
    private static double calcE6(ExcelTrialBalanceExcelRowHelper bsExcel) {
        double externalLoan = 0; // todo: if there is a ever a ledger number then dont forget to put this
        double totalAssets  = getTotalAssets(bsExcel);
        if (totalAssets == 0) return 0;
        return (externalLoan / totalAssets) * 100.0;
    }

    /**
     * E7: शेयर पुँजी — Member share capital ÷ Total Assets × 100
     * TODO: replace with the correct TrialBalanceEnum key for share capital
     */
    private static double calcE7(ExcelTrialBalanceExcelRowHelper bsExcel) {
        double shareCapital = bsExcel.getCredit(TrialBalanceEnum.SHARE_CAPITAL); // adjust key
        double totalAssets  = getTotalAssets(bsExcel);
        if (totalAssets == 0) return 0;
        return (shareCapital / totalAssets) * 100.0;
    }

    /**
     * E8: संस्थागत पुँजी — Reserve fund + Loss provision fund + Land & building fund
     *                      + Capital grants + Unspent funds ÷ Total Assets × 100
     * TODO: replace with the correct TrialBalanceEnum keys for each reserve/fund ledger
     */
    private static double calcE8(ExcelTrialBalanceExcelRowHelper bsExcel) {
        double institutionalCapital = bsExcel.getCreditSum(
                TrialBalanceEnum.RESERVE_FUND, TrialBalanceEnum.DUBANTI_KOSH,
                TrialBalanceEnum.GHATA_PURTI_KOSH, TrialBalanceEnum.PUGIGAT_JAGAEDA_KOSH,
                TrialBalanceEnum.LOSS_FULFILMENT_FUND, TrialBalanceEnum.COOPERATIVE_PROMOTION_FUND,
                TrialBalanceEnum.COOPERATIVE_DEVELOPMENT_FUND, TrialBalanceEnum.STHIRKARAN_KOSH,
                TrialBalanceEnum.SAMUDAIYIK_BIKASH_KOSH, TrialBalanceEnum.JIBIKOPARCHAN_KOSH,
                TrialBalanceEnum.OTHER_RISK_MANAGEMENT_FUND
        );
        double totalAssets = getTotalAssets(bsExcel);
        if (totalAssets == 0) return 0;
        return (institutionalCapital / totalAssets) * 100.0;
    }

    private static double getTotalAssets(ExcelTrialBalanceExcelRowHelper bsExcel) {
        return bsExcel.getTotalCredit(); // adjust key
    }

    // ── Row/cell helpers ─────────────────────────────────────────────────────

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

        int[] ordinalByCol = {3, 2, 1, 0};
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
