package com.progress.coolProject.DTO.Excel.Slides.fortyto50;

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
public class SlideFourtyNine {

    // Column headers
    private static final String COLUMN_INDICATOR = "संकेतहरु";
    private static final String COLUMN_EMPTY_1   = "";
    private static final String COLUMN_EMPTY_2   = "";
    private static final String COLUMN_MEASURE   = "हुनुपर्ने\n(लक्ष्य)";
    private static final String COLUMN_Q4        = "चौथो\nत्रैमासिक";
    private static final String COLUMN_Q3        = "तेस्रो\nत्रैमासिक";
    private static final String COLUMN_Q2        = "दोस्रो\nत्रैमासिक";
    private static final String COLUMN_Q1        = "पहिलो\nत्रैमासिक";

    // यस टेन — सदस्य वृद्धिदर
    private static final String ROW_G10_IND  = "यस टेन";
    private static final String ROW_G10_DET  = "सदस्य\nवृद्धिदर";
    private static final String ROW_G10_DESC = "यस बर्ष - गत\nबर्ष भागा गत बर्ष";
    private static final String ROW_G10_TGT  = "कम्तिमा\n१२%";

    // यस इलावेन — कूल सम्पत्ति वृद्धिदर
    private static final String ROW_G11_IND  = "यस इलावेन";
    private static final String ROW_G11_DET  = "कूल सम्पत्ति\nवृद्धिदर";
    private static final String ROW_G11_DESC = "यस बर्ष - गत\nबर्ष भागा गत बर्ष";
    private static final String ROW_G11_TGT  = "मुद्रास्फीति\nदर भन्दा\nबढी";

    private static final double      NORMAL_FONT_SIZE    = 14.0;
    private static final double      HEADER_FONT_SIZE    = 14.0;
    private static final PresetColor NORMAL_FONT_COLOR   = PresetColor.BLACK;
    private static final Color       NORMAL_BORDER_COLOR = Color.BLACK;

    /**
     * @param ppt                  the slide show
     * @param slideTitle           slide title string
     * @param excel                current year trial balance helper
     * @param previousYearMembers  total member count as of end of last fiscal year
     * @param currentYearMembers   total member count as of current quarter end
     * @param previousYearAssets   total assets at end of last fiscal year (in your unit, e.g. lakhs)
     */
    public static void createDataSlide(XMLSlideShow ppt,
                                       String slideTitle,
                                       ExcelTrialBalanceExcelRowHelper excel,
                                       long previousYearMembers,
                                       long currentYearMembers,
                                       double previousYearAssets) {

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

        double g10Value = calcS10(previousYearMembers, currentYearMembers);
        double g11Value = calcS11(excel, previousYearAssets);

        fillRow(table, 1, ROW_G10_IND, ROW_G10_DET, ROW_G10_DESC, ROW_G10_TGT,
                currentQuarter, g10Value, nepFmt, lightOrange);

        fillRow(table, 2, ROW_G11_IND, ROW_G11_DET, ROW_G11_DESC, ROW_G11_TGT,
                currentQuarter, g11Value, nepFmt, lightBlue);
    }

    // ── Calculations ──────────────────────────────────────────────────────────

    /**
     * S10: सदस्य वृद्धिदर
     * = (Current Year Members − Previous Year Members) ÷ Previous Year Members × 100
     * <p>
     * Member counts are NOT in the trial balance — they must be passed in
     * from your membership register / database.
     */
    private static double calcS10(long previousYearMembers, long currentYearMembers) {
        if (previousYearMembers == 0) return 0;
        return ((double)(currentYearMembers - previousYearMembers) / previousYearMembers) * 100.0;
    }

    /**
     * S11: कूल सम्पत्ति वृद्धिदर
     * = (Current Total Assets − Previous Year Total Assets) ÷ Previous Year Total Assets × 100
     * <p>
     * Current total assets are derived from trial balance.
     * Previous year total assets must be passed in (prior year closing balance sheet).
     */
    private static double calcS11(ExcelTrialBalanceExcelRowHelper bsExcel,
                                  double previousYearAssets) {
        if (previousYearAssets == 0) return 0;
        double currentAssets = bsExcel.getTotalCredit();
        return ((currentAssets - previousYearAssets) / previousYearAssets) * 100.0;
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
