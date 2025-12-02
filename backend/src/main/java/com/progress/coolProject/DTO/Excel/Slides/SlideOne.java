package com.progress.coolProject.DTO.Excel.Slides;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import com.progress.coolProject.Enums.TrialBalanceEnum;
import com.progress.coolProject.Utils.Excel.ExcelTrialBalanceExcelRowHelper;
import com.progress.coolProject.Utils.PowerPoint.PPTUtils;
import lombok.experimental.UtilityClass;
import org.apache.poi.sl.usermodel.VerticalAlignment;
import org.apache.poi.xddf.usermodel.PresetColor;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.text.TextAlignment;
import org.apache.poi.xddf.usermodel.text.XDDFTextParagraph;
import org.apache.poi.xddf.usermodel.text.XDDFTextRun;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;

@UtilityClass
public final class SlideOne {
    // Header
    public static final String FIRST_ROW_TITLE = "२०८२ असोज मसान्तसम्मको वासालात";

    // Column Headers
    public static final String COLUMN_CAPITAL_LIABILITY = "पूँजी तथा दायित्व";
    public static final String COLUMN_AMOUNT = "रकम";
    public static final String COLUMN_PERCENTAGE = "प्रतिशत";
    public static final String COLUMN_ASSETS = "सम्पत्ति";

    // Left Side Row Labels (Capital & Liability)
    public static final String ROW_SHARE_CAPITAL = "शेयरपुँजी";
    public static final String ROW_RESERVE_FUND = "जग्गेडा कोष";
    public static final String ROW_OTHER_FUND = "अन्य कोष";
    public static final String ROW_SAVINGS_DEPOSIT = "बचत निक्षेप";
    public static final String ROW_GRANT = "ब्याज मुल्ताबी हिसाब";
    public static final String ROW_PAYABLE = "भू.दिनुपर्ने";
    public static final String ROW_OTHER_LIABILITY = "अन्य दायित्व";
    public static final String ROW_PROFIT_LOSS_LEFT = "नाफा (नोकसान)";
    public static final String ROW_TOTAL_LEFT = "जम्मा रु.";

    // Right Side Row Labels (Assets)
    public static final String ROW_CASH = "नगद";
    public static final String ROW_BANK = "बैंक";
    public static final String ROW_INVESTMENT = "लगानी";
    public static final String ROW_LOAN_TO_MEMBERS = "सदस्यलाई दिएको ऋण";
    public static final String ROW_RECEIVABLE = "पाउनुपर्ने हिसाब";
    public static final String ROW_FIXED_ASSETS = "स्थीर सम्पत्ति";
    public static final String ROW_OTHER_ASSETS = "अन्य सम्पत्ति";
    public static final String ROW_PROFIT_LOSS_RIGHT = "नाफा (नोकसान)";
    public static final String ROW_TOTAL_RIGHT = "जम्मा रु.";


    private static final double NORMAL_FONT_SIZE = 11.0;
    private static final PresetColor NORMAL_FONT_COLOR = PresetColor.BLACK;
    private static final Color NORMAL_BORDER_COLOR = Color.WHITE;

    public static Double getShareCapital(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCredit(TrialBalanceEnum.SHARE_CAPITAL);
    }

    public static Double getReserveFund(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCredit(TrialBalanceEnum.RESERVE_FUND);
    }

    public static Double getOtherFunds(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(
                TrialBalanceEnum.RAHAT_KOSH, TrialBalanceEnum.JIBIKOPARCHAN_KOSH,
                TrialBalanceEnum.OTHER_RISK_MANAGEMENT_FUND, TrialBalanceEnum.SAMUDAIYIK_BIKASH_KOSH,
                TrialBalanceEnum.STHIRKARAN_KOSH, TrialBalanceEnum.DUBANTI_KOSH,
                TrialBalanceEnum.EMPLOYEE_BONUS_FUND, TrialBalanceEnum.COOPERATIVE_EDUCATION_FUND,
                TrialBalanceEnum.COOPERATIVE_DEVELOPMENT_FUND, TrialBalanceEnum.LOSS_FULFILMENT_FUND,
                TrialBalanceEnum.LOAN_LOSS_PROVISION, TrialBalanceEnum.COOPERATIVE_PROMOTION_FUND,
                TrialBalanceEnum.SHARE_DIVIDEND_FUND, TrialBalanceEnum.SECURED_CAPITAL_REDEMPTION_FUND
                );
    }

    public static Double getSavingsDeposit(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(TrialBalanceEnum.SAVING_ACCOUNT);
    }

    public static Double getGrant(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(TrialBalanceEnum.BYAG_MULTABI_HISAB);
    }

    public static Double getPayable(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(
                TrialBalanceEnum.KRISHI_BIKASH_SPECIAL_FD_INTEREST, TrialBalanceEnum.INTEREST_TAX,
                TrialBalanceEnum.OTHER_PAYABLE, TrialBalanceEnum.SALARY_PAYABLE,
                TrialBalanceEnum.AUDIT_FEE_PAYABLE, TrialBalanceEnum.TDS_SOCIAL_SECURITY_TAX,
                TrialBalanceEnum.TDS_AUDIT_FEE, TrialBalanceEnum.BAITHAK_BHATTA_TAX,
                TrialBalanceEnum.TDS_PAYABLE, TrialBalanceEnum.INTEREST_PAYABLE,
                TrialBalanceEnum.ADVANCES_PAYABLE, TrialBalanceEnum.BHAJANGAL_SAHAKARI
        );
    }

    public static Double getOtherLiability(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(TrialBalanceEnum.OTHER_LIABILITIES);
    }

    public static Double getProfitLossLeft(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(TrialBalanceEnum.CURRENT_YEAR_PROFIT_LOSS);
    }

    public static Double getTotalLeft(ExcelTrialBalanceExcelRowHelper excel) {
        return getShareCapital(excel) + getReserveFund(excel) + getOtherFunds(excel)
                + getSavingsDeposit(excel) + getGrant(excel) + getPayable(excel)
                + getOtherLiability(excel) + getProfitLossLeft(excel);
    }

    public static Double getCash(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getDebitSum(TrialBalanceEnum.CASH);
    }

    public static Double getBank(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getDebitSum(
                TrialBalanceEnum.KASKUN_REGULAR_SAVING,
                TrialBalanceEnum.KRISHI_BIKASH_BANK, TrialBalanceEnum.KASKUN_DAINIK,
                TrialBalanceEnum.BANK_DEVIDEND_SAVING, TrialBalanceEnum.NEPAL_INV_BANK,
                TrialBalanceEnum.KASKUN_TIME_SAVING, TrialBalanceEnum.SANIMA_BANK,
                TrialBalanceEnum.RSB_TIME_SAVING, TrialBalanceEnum.NATIONAL_COOPERATIVE,
                TrialBalanceEnum.NEFSCUN_REGULAR_SAVING
        );
    }

    public static Double getInvestment(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getDebitSum(
                TrialBalanceEnum.SHARE_NEFSCUN, TrialBalanceEnum.SHARE_INVEST_NCBL,
                TrialBalanceEnum.SHARE_JILLA_SAHAKARI, TrialBalanceEnum.SHARE_KASKUN

        );
    }

    public static Double getLoanToMembers(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getDebitSum(
                TrialBalanceEnum.LOAN_ACCOUNT
        );
    }

    public static Double getReceivable(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getDebitSum(
                TrialBalanceEnum.TDS_RECEIVABLES_ADVANCE_TAX, TrialBalanceEnum.TDS_ON_INTEREST_RECEIVABLE,
                TrialBalanceEnum.ADVANCES_RECEIVABLES
        );
    }

    public static Double getFixedAssets(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getDebitSum(
                TrialBalanceEnum.LAND, TrialBalanceEnum.BUILDING,
                TrialBalanceEnum.LAND_AND_BUILDING
        );
    }

    public static Double getOtherAssets(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getDebitSum(
                TrialBalanceEnum.FURNITURE, TrialBalanceEnum.OFFICE_GOODS,
                TrialBalanceEnum.OFFICE_EQUIPMENTS
        );
    }

    public static Double getProfitLossRight(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getDebitSum(TrialBalanceEnum.CURRENT_YEAR_PROFIT_LOSS);
    }

    public static Double getTotalRight(ExcelTrialBalanceExcelRowHelper excel) {
        return getCash(excel) + getBank(excel) + getInvestment(excel)
                + getLoanToMembers(excel) + getReceivable(excel) + getFixedAssets(excel)
                + getOtherAssets(excel) + getProfitLossRight(excel);
    }

    public static void createDataSlide(XMLSlideShow ppt, String slideTitle,
                                       ExcelTrialBalanceExcelRowHelper data) {
        XSLFSlide slide = ppt.createSlide();

        // Configure Nepali number formatter
        NumberFormat nepaliFormat = NumberFormat.getInstance(new ULocale("ne_NP"));
        nepaliFormat.setMinimumFractionDigits(2);

        PPTUtils.makeTitleForSlide(slide, slideTitle);

        // Create table structure: 2 main sections (left and right)
        // Left: Capital & Liability | Amount | Percentage
        // Right: Assets | Amount | Percentage
        int numRows = 11; // 1 header + 8 data rows + 1 total row
        int numCols = 6;  // 3 columns for left section + 3 columns for right section

        XSLFTable table = slide.createTable(numRows, numCols);
        table.setAnchor(new Rectangle(20, 60, 680, 400));

        // Set column widths
        table.setColumnWidth(0, 150); // Left - Description
        table.setColumnWidth(1, 90);  // Left - Amount
        table.setColumnWidth(2, 80);  // Left - Percentage
        table.setColumnWidth(3, 150); // Right - Description
        table.setColumnWidth(4, 90);  // Right - Amount
        table.setColumnWidth(5, 80);  // Right - Percentage

        // ROW 0: Merged title row (merge all 6 cells)
        XSLFTableCell mergedCell = table.getCell(0, 0);
        mergedCell.setText(FIRST_ROW_TITLE);
        mergedCell.setFillColor(new Color(79, 129, 189)); // Blue background
        mergedCell.setVerticalAlignment(VerticalAlignment.MIDDLE);

        table.mergeCells(0, 0, 0, 5); // (firstRow, lastRow, firstCol, lastCol)

        XDDFTextParagraph mergedPara = mergedCell.getTextBody().getParagraphs().getFirst();
        mergedPara.setTextAlignment(TextAlignment.CENTER);
        XDDFTextRun mergedRun = mergedPara.getTextRuns().getFirst();
        mergedRun.setBold(true);
        mergedRun.setFontSize(14.0);
        mergedRun.setFontColor(XDDFColor.from(255, 255, 255)); // White text

        // Style for header cells (yellow background)
        Color headerYellow = new Color(255, 255, 0);

        // Header row (row 0)
        String[] headers = {
                COLUMN_CAPITAL_LIABILITY, COLUMN_AMOUNT, COLUMN_PERCENTAGE,
                COLUMN_ASSETS, COLUMN_AMOUNT, COLUMN_PERCENTAGE
        };

        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(1, col);
            PPTUtils.setCellTextWithStyle(cell,
                    headers[col], TextAlignment.CENTER, PresetColor.BLACK,
                    12.0, true,
                    headerYellow, NORMAL_BORDER_COLOR);
        }

        // Data rows (alternating white and light blue)
        Color lightBlue = new Color(217, 225, 242);

        Double totalLeft = getTotalLeft(data);
        Double totalRight = getTotalRight(data);

        // Row 1: Share Capital
        int row = 2;
        fillLeftSection(table, row, ROW_SHARE_CAPITAL, getShareCapital(data),
                totalLeft, nepaliFormat, lightBlue);
        fillRightSection(table, row, ROW_CASH, getCash(data), totalRight, nepaliFormat, lightBlue);

        // Row 2: Reserve Fund
        row = 3;
        fillLeftSection(table, row, ROW_RESERVE_FUND, getReserveFund(data),
                totalLeft, nepaliFormat, Color.WHITE);
        fillRightSection(table, row, ROW_BANK, getBank(data), totalRight, nepaliFormat, Color.WHITE);

        // Row 3: Other Funds
        row = 4;
        fillLeftSection(table, row, ROW_OTHER_FUND, getOtherFunds(data),
                totalLeft, nepaliFormat, lightBlue);
        fillRightSection(table, row, ROW_INVESTMENT, getInvestment(data), totalRight, nepaliFormat, lightBlue);

        // Row 4: Savings Deposit
        row = 5;
        fillLeftSection(table, row, ROW_SAVINGS_DEPOSIT, getSavingsDeposit(data),
                totalLeft, nepaliFormat, Color.WHITE);
        fillRightSection(table, row, ROW_LOAN_TO_MEMBERS, getLoanToMembers(data), totalRight, nepaliFormat, Color.WHITE);

        // Row 5: Grant
        row = 6;
        fillLeftSection(table, row, ROW_GRANT, getGrant(data),
                totalLeft, nepaliFormat, lightBlue);
        fillRightSection(table, row, ROW_RECEIVABLE, getReceivable(data), totalRight, nepaliFormat, lightBlue);

        // Row 6: Payable
        row = 7;
        fillLeftSection(table, row, ROW_PAYABLE, getPayable(data),
                totalLeft, nepaliFormat, Color.WHITE);
        fillRightSection(table, row, ROW_FIXED_ASSETS, getFixedAssets(data), totalRight, nepaliFormat, Color.WHITE);

        // Row 7: Other Liability
        row = 8;
        fillLeftSection(table, row, ROW_OTHER_LIABILITY, getOtherLiability(data),
                totalLeft, nepaliFormat, lightBlue);
        fillRightSection(table, row, ROW_OTHER_ASSETS, getOtherAssets(data), totalRight, nepaliFormat, lightBlue);

        // Row 8: Profit/Loss
        row = 9;
        fillLeftSection(table, row, ROW_PROFIT_LOSS_LEFT, getProfitLossLeft(data),
                totalLeft, nepaliFormat, Color.WHITE);
        fillRightSection(table, row, ROW_PROFIT_LOSS_RIGHT, getProfitLossRight(data), totalRight, nepaliFormat, Color.WHITE);

        // Row 9: Total (blue background)
        row = 10;
        Color totalBlue = new Color(79, 129, 189);

        fillLeftSection(table, row, ROW_TOTAL_LEFT, totalLeft,
                totalLeft, nepaliFormat, totalBlue);
        fillRightSection(table, row, ROW_TOTAL_RIGHT, totalRight,
                totalRight, nepaliFormat, totalBlue);

        // Make total row text white and bold
        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(row, col);
            PPTUtils.setCellTextWithStyle(cell,
                    null, null, PresetColor.WHITE,
                    NORMAL_FONT_SIZE, true,
                    totalBlue, NORMAL_BORDER_COLOR);
        }
    }

    // Helper method to fill left section (Capital & Liability)
    private static void fillLeftSection(XSLFTable table, int row, String description,
                                        Double amount, Double total, NumberFormat formatter, Color bgColor) {
        // Description
        XSLFTableCell cell0 = table.getCell(row, 0);
        PPTUtils.setCellTextWithStyle(cell0,
                description, TextAlignment.LEFT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Amount
        XSLFTableCell cell1 = table.getCell(row, 1);
        PPTUtils.setCellTextWithStyle(cell1,
                formatter.format(amount), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Percentage
        XSLFTableCell cell2 = table.getCell(row, 2);
        double percentage = total != 0 ? (amount / total) * 100 : 0.0;
        PPTUtils.setCellTextWithStyle(cell2,
                formatter.format(percentage), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);
    }

    // Helper method to fill right section (Assets)
    private static void fillRightSection(XSLFTable table, int row, String description,
                                         Double amount, Double total, NumberFormat formatter, Color bgColor) {
        // Description
        XSLFTableCell cell3 = table.getCell(row, 3);
        PPTUtils.setCellTextWithStyle(cell3,
                description, TextAlignment.LEFT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Amount
        XSLFTableCell cell4 = table.getCell(row, 4);
        PPTUtils.setCellTextWithStyle(cell4,
                formatter.format(amount), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);

        // Percentage
        XSLFTableCell cell5 = table.getCell(row, 5);
        double percentage = total != 0 ? (amount / total) * 100 : 0.0;
        PPTUtils.setCellTextWithStyle(cell5,
                formatter.format(percentage), TextAlignment.RIGHT, NORMAL_FONT_COLOR,
                NORMAL_FONT_SIZE, false,
                bgColor, NORMAL_BORDER_COLOR);
    }
}
