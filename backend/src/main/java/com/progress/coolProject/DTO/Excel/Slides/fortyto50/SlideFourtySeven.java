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
public class SlideFourtySeven {

    // Column headers
    private static final String COLUMN_INDICATOR = "संकेतहरु";
    private static final String COLUMN_EMPTY_1   = "";
    private static final String COLUMN_EMPTY_2   = "";
    private static final String COLUMN_MEASURE   = "हुनुपर्ने\n(लक्ष्य)";
    private static final String COLUMN_Q4        = "चौथो\nत्रैमासिक";
    private static final String COLUMN_Q3        = "तेस्रो\nत्रैमासिक";
    private static final String COLUMN_Q2        = "दोस्रो\nत्रैमासिक";
    private static final String COLUMN_Q1        = "पहिलो\nत्रैमासिक";

    // आर नाईन — संचालन खर्च
    private static final String ROW_R9_IND  = "आर नाईन";
    private static final String ROW_R9_DET  = "संचालन खर्च";
    private static final String ROW_R9_DESC = "संस्था संचालन गर्दा\nलागेको प्रशासनिक\nसबै खर्च रकमहरु";
    private static final String ROW_R9_TGT  = "५% भन्दा\nकम";

    // आर टेन — ऋण जोखिम व्यहोर्ने खर्च
    private static final String ROW_R10_IND  = "आर टेन";
    private static final String ROW_R10_DET  = "ऋण जोखिम\nव्यहोर्ने खर्च";
    private static final String ROW_R10_DESC = "संस्थाले गरेको ऋण\nजोखिम व्यवस्था\nखर्च रकम";
    private static final String ROW_R10_TGT  = "पर्याप्त\nजोखिम\nव्यवस्था\nहुने गरी";

    private static final double      NORMAL_FONT_SIZE    = 14.0;
    private static final double      HEADER_FONT_SIZE    = 14.0;
    private static final PresetColor NORMAL_FONT_COLOR   = PresetColor.BLACK;
    private static final Color       NORMAL_BORDER_COLOR = Color.BLACK;

    public static void createDataSlide(XMLSlideShow ppt,
                                       String slideTitle,
                                       ExcelTrialBalanceExcelRowHelper plExcel,
                                       ExcelTrialBalanceExcelRowHelper bsExcel,
                                       double previousMonthTotalBalanceSheetCredit
                                       ) {

        XSLFSlide slide = ppt.createSlide();

        NumberFormat nepFmt = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepFmt.setMinimumFractionDigits(2);
        nepFmt.setMaximumFractionDigits(2);

        PPTUtils.makeTitleForSlide(slide, slideTitle);

        // 1 header row + 2 data rows, 8 columns
        XSLFTable table = slide.createTable(3, 8);
        table.setAnchor(new Rectangle(10, 60, 700, 380));

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

        double r9Value  = calcR9(plExcel);
        double r10Value = calcR10(plExcel);

        fillRow(table, 1, ROW_R9_IND,  ROW_R9_DET,  ROW_R9_DESC,  ROW_R9_TGT,
                currentQuarter, r9Value,  nepFmt, lightOrange);

        fillRow(table, 2, ROW_R10_IND, ROW_R10_DET, ROW_R10_DESC, ROW_R10_TGT,
                currentQuarter, r10Value, nepFmt, lightBlue);
    }

    // ── Calculations ──────────────────────────────────────────────────────────

    /**
     * R9: संचालन खर्च
     * = Total Operating/Administrative Expenses ÷ Total Assets × 100
     *
     * All admin/operating expense ledgers from TrialBalanceEnum (debit-side expenses):
     */
    private static double calcR9(ExcelTrialBalanceExcelRowHelper excel) {
        double operatingExpenses = excel.getDebitSum(
                TrialBalanceEnum.SALARY,
                TrialBalanceEnum.TELEPHONE_EXPENSE,
                TrialBalanceEnum.ELECTRICITY_EXPENSE,
                TrialBalanceEnum.WATER_EXPENSE,
                TrialBalanceEnum.PRINTING_EXPENSE,
                TrialBalanceEnum.STATIONARY_EXPENSE,
                TrialBalanceEnum.TRAVELLING_EXPENSE,
                TrialBalanceEnum.FINE_AND_PENALTY,
                TrialBalanceEnum.AUDIT_FEE_EXPENSE,
                TrialBalanceEnum.DASHAIN_ALLOWANCE,
                TrialBalanceEnum.TIFFIN_EXPENSE,
                TrialBalanceEnum.GENERAL_MEETING,
                TrialBalanceEnum.REPAIR_AND_MAINTENANCE_CHARGE,
                TrialBalanceEnum.TRAINING_CHARGE_EXPENSE,
                TrialBalanceEnum.MISCELLANEOUS_EXPENSE,
                TrialBalanceEnum.BONUS_EXPENCES,
                TrialBalanceEnum.OFFICE_EXPENSE,
                TrialBalanceEnum.AGM_EXPENSE,
                TrialBalanceEnum.FINE_AND_PENALTIES_EXPENSES,
                TrialBalanceEnum.OFFICE_CLEANING,
                TrialBalanceEnum.AMC_EXPENSE,
                TrialBalanceEnum.CLOUD_EXPENSE,
                TrialBalanceEnum.RAHAT_KHARCHA,
                TrialBalanceEnum.CURBS_FEE,
                TrialBalanceEnum.SANTONA_KHARCHA,
                TrialBalanceEnum.PROVIDENT_EXP,
                TrialBalanceEnum.SOCIAL_PROGRAMME,
                TrialBalanceEnum.VAT_EXP,
                TrialBalanceEnum.SANJAL_MEMBER_FEE,
                TrialBalanceEnum.DRESS
        );

        double totalAssets = getTotalAssets(excel);
        if (totalAssets == 0) return 0;
        return (operatingExpenses / totalAssets) * 100.0;
    }

    /**
     * R10: ऋण जोखिम व्यहोर्ने खर्च
     * = Loan Loss Provision Expense ÷ Total Loan Portfolio × 100
     *
     * Numerator:   LOAN_LOSS_PROVISION_EXPENSES (9901) — LLP expense booked this period
     * Denominator: LOAN_ACCOUNT (3001)                 — total loan portfolio
     */
    private static double calcR10(ExcelTrialBalanceExcelRowHelper excel) {
        double llpExpense = excel.getDebit(TrialBalanceEnum.LOAN_LOSS_PROVISION_EXPENSES);
        double totalLoan  = excel.getDebit(TrialBalanceEnum.LOAN_ACCOUNT);
        if (totalLoan == 0) return 0;
        return (llpExpense / totalLoan) * 100.0;
    }

    /**
     * Total Assets — consistent with SlideFourtyFour.
     * Sum of all debit-side balance sheet items.
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
