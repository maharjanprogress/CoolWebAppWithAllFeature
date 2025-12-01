package com.progress.coolProject.DTO.Excel.Slides;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import com.progress.coolProject.Enums.TrialBalanceEnum;
import com.progress.coolProject.Utils.Excel.ExcelTrialBalanceExcelRowHelper;
import lombok.experimental.UtilityClass;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.sl.usermodel.VerticalAlignment;
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
    public static final String ROW_GRANT = "अनुदान";
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
        return 0.0;
    }

    public static Double getPayable(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(
                TrialBalanceEnum.KRISHI_BIKASH_SPECIAL_FD_INTEREST, TrialBalanceEnum.INTEREST_TAX,
                TrialBalanceEnum.OTHER_PAYABLE, TrialBalanceEnum.SALARY_PAYABLE,
                TrialBalanceEnum.AUDIT_FEE_PAYABLE, TrialBalanceEnum.TDS_SOCIAL_SECURITY_TAX,
                TrialBalanceEnum.TDS_AUDIT_FEE, TrialBalanceEnum.BAITHAK_BHATTA_TAX,
                TrialBalanceEnum.TDS_PAYABLE, TrialBalanceEnum.INTEREST_PAYABLE
        );
    }

    public static Double getOtherLiability(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getCreditSum(TrialBalanceEnum.OTHER_LIABILITIES);
    }

    public static Double getProfitLossLeft(ExcelTrialBalanceExcelRowHelper excel) {
        return 0.0;
    }

    public static Double getTotalLeft(ExcelTrialBalanceExcelRowHelper excel) {
        return getShareCapital(excel) + getReserveFund(excel) + getOtherFunds(excel)
                + getSavingsDeposit(excel) + getGrant(excel) + getPayable(excel)
                + getOtherLiability(excel) + getProfitLossLeft(excel);
    }

//    todo: complete the right side functions

    public static Double getCash(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getDebitSum(TrialBalanceEnum.CASH);
    }

    public static Double getBank(ExcelTrialBalanceExcelRowHelper excel) {
        return excel.getDebitSum(
                TrialBalanceEnum.KASKUN_REGULAR,
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
        return 0.0;
    }

    public static Double getFixedAssets(ExcelTrialBalanceExcelRowHelper excel) {
        return 0.0;
    }

    public static Double getOtherAssets(ExcelTrialBalanceExcelRowHelper excel) {
        return 0.0;
    }

    public static Double getProfitLossRight(ExcelTrialBalanceExcelRowHelper excel) {
        return 0.0;
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

        // Add slide title with yellow background (top)
        XSLFTextBox slideTitleBox = slide.createTextBox();
        slideTitleBox.setAnchor(new Rectangle(0, 0, 720, 50));
        slideTitleBox.setFillColor(new Color(255, 204, 102)); // Yellow background

        XSLFTextParagraph slideTitlePara = slideTitleBox.addNewTextParagraph();
        slideTitlePara.setTextAlign(TextParagraph.TextAlign.CENTER);
        XSLFTextRun slideTitleRun = slideTitlePara.addNewTextRun();
        slideTitleRun.setText(slideTitle);
        slideTitleRun.setFontSize(18.0);
        slideTitleRun.setBold(true);
        slideTitleRun.setFontColor(Color.BLACK);

        // Add table title with blue background (below slide title)
        XSLFTextBox tableTitleBox = slide.createTextBox();
        tableTitleBox.setAnchor(new Rectangle(0, 50, 720, 40));
        tableTitleBox.setFillColor(new Color(79, 129, 189)); // Blue background

        XSLFTextParagraph tableTitlePara = tableTitleBox.addNewTextParagraph();
        tableTitlePara.setTextAlign(TextParagraph.TextAlign.CENTER);
        XSLFTextRun tableTitleRun = tableTitlePara.addNewTextRun();
        tableTitleRun.setText(FIRST_ROW_TITLE);
        tableTitleRun.setFontSize(16.0);
        tableTitleRun.setBold(false);
        tableTitleRun.setFontColor(Color.WHITE);

        // Create table structure: 2 main sections (left and right)
        // Left: Capital & Liability | Amount | Percentage
        // Right: Assets | Amount | Percentage
        int numRows = 10; // 1 header + 8 data rows + 1 total row
        int numCols = 6;  // 3 columns for left section + 3 columns for right section

        XSLFTable table = slide.createTable(numRows, numCols);
        table.setAnchor(new Rectangle(20, 110, 680, 400));

        // Set column widths
        table.setColumnWidth(0, 150); // Left - Description
        table.setColumnWidth(1, 90);  // Left - Amount
        table.setColumnWidth(2, 80);  // Left - Percentage
        table.setColumnWidth(3, 150); // Right - Description
        table.setColumnWidth(4, 90);  // Right - Amount
        table.setColumnWidth(5, 80);  // Right - Percentage

        // Style for header cells (yellow background)
        Color headerYellow = new Color(255, 255, 0);

        // Header row (row 0)
        String[] headers = {
                COLUMN_CAPITAL_LIABILITY, COLUMN_AMOUNT, COLUMN_PERCENTAGE,
                COLUMN_ASSETS, COLUMN_AMOUNT, COLUMN_PERCENTAGE
        };

        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(0, col);
            cell.setText(headers[col]);
            cell.setFillColor(headerYellow);
            cell.setVerticalAlignment(VerticalAlignment.MIDDLE);

            XDDFTextParagraph para = cell.getTextBody().getParagraphs().getFirst();
            para.setTextAlignment(TextAlignment.CENTER);
            XDDFTextRun run = para.getTextRuns().getFirst();
            run.setBold(true);
            run.setFontSize(11.0);
        }

        // Data rows (alternating white and light blue)
        Color lightBlue = new Color(217, 225, 242);

        Double totalLeft = getTotalLeft(data);
        Double totalRight = getTotalRight(data);

        // Row 1: Share Capital
        int row = 1;
        fillLeftSection(table, row, ROW_SHARE_CAPITAL, getShareCapital(data),
                totalLeft, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);
        fillRightSection(table, row, ROW_CASH, getCash(data), totalRight, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);

        // Row 2: Reserve Fund
        row = 2;
        fillLeftSection(table, row, ROW_RESERVE_FUND, getReserveFund(data),
                totalLeft, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);
        fillRightSection(table, row, ROW_BANK, getBank(data), totalRight, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);

        // Row 3: Other Funds
        row = 3;
        fillLeftSection(table, row, ROW_OTHER_FUND, getOtherFunds(data),
                totalLeft, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);
        fillRightSection(table, row, ROW_INVESTMENT, getInvestment(data), totalRight, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);

        // Row 4: Savings Deposit
        row = 4;
        fillLeftSection(table, row, ROW_SAVINGS_DEPOSIT, getSavingsDeposit(data),
                totalLeft, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);
        fillRightSection(table, row, ROW_LOAN_TO_MEMBERS, getLoanToMembers(data), totalRight, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);

        // Row 5: Grant
        row = 5;
        fillLeftSection(table, row, ROW_GRANT, getGrant(data),
                totalLeft, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);
        fillRightSection(table, row, ROW_RECEIVABLE, getReceivable(data), totalRight, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);

        // Row 6: Payable
        row = 6;
        fillLeftSection(table, row, ROW_PAYABLE, getPayable(data),
                totalLeft, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);
        fillRightSection(table, row, ROW_FIXED_ASSETS, getFixedAssets(data), totalRight, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);

        // Row 7: Other Liability
        row = 7;
        fillLeftSection(table, row, ROW_OTHER_LIABILITY, getOtherLiability(data),
                totalLeft, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);
        fillRightSection(table, row, ROW_OTHER_ASSETS, getOtherAssets(data), totalRight, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);

        // Row 8: Profit/Loss
        row = 8;
        fillLeftSection(table, row, ROW_PROFIT_LOSS_LEFT, getProfitLossLeft(data),
                totalLeft, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);
        fillRightSection(table, row, ROW_PROFIT_LOSS_RIGHT, getProfitLossRight(data), totalRight, nepaliFormat, row % 2 == 0 ? Color.WHITE : lightBlue);

        // Row 9: Total (blue background)
        row = 9;
        Color totalBlue = new Color(79, 129, 189);

        fillLeftSection(table, row, ROW_TOTAL_LEFT, totalLeft,
                totalLeft, nepaliFormat, totalBlue);
        fillRightSection(table, row, ROW_TOTAL_RIGHT, totalRight,
                totalRight, nepaliFormat, totalBlue);

        // Make total row text white and bold
        for (int col = 0; col < numCols; col++) {
            XSLFTableCell cell = table.getCell(row, col);
            XDDFTextParagraph para = cell.getTextBody().getParagraphs().getFirst();
            XDDFTextRun run = para.getTextRuns().getFirst();
            run.setBold(true);
            run.setFontColor(XDDFColor.from(255, 255, 255));
        }
    }

    // Helper method to fill left section (Capital & Liability)
    private static void fillLeftSection(XSLFTable table, int row, String description,
                                        Double amount, Double total, NumberFormat formatter, Color bgColor) {
        // Description
        XSLFTableCell cell0 = table.getCell(row, 0);
        cell0.setText(description);
        cell0.setFillColor(bgColor);
        cell0.setVerticalAlignment(VerticalAlignment.MIDDLE);

        // Amount
        XSLFTableCell cell1 = table.getCell(row, 1);
        cell1.setText(formatter.format(amount));
        cell1.setFillColor(bgColor);
        cell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
        XDDFTextParagraph para1 = cell1.getTextBody().getParagraphs().getFirst();
        para1.setTextAlignment(TextAlignment.RIGHT);

        // Percentage
        XSLFTableCell cell2 = table.getCell(row, 2);
        double percentage = total != 0 ? (amount / total) * 100 : 0.0;
        cell2.setText(formatter.format(percentage));
        cell2.setFillColor(bgColor);
        cell2.setVerticalAlignment(VerticalAlignment.MIDDLE);
        XDDFTextParagraph para2 = cell2.getTextBody().getParagraphs().getFirst();
        para2.setTextAlignment(TextAlignment.RIGHT);
    }

    // Helper method to fill right section (Assets)
    private static void fillRightSection(XSLFTable table, int row, String description,
                                         Double amount, Double total, NumberFormat formatter, Color bgColor) {
        // Description
        XSLFTableCell cell3 = table.getCell(row, 3);
        cell3.setText(description);
        cell3.setFillColor(bgColor);
        cell3.setVerticalAlignment(VerticalAlignment.MIDDLE);

        // Amount
        XSLFTableCell cell4 = table.getCell(row, 4);
        cell4.setText(formatter.format(amount));
        cell4.setFillColor(bgColor);
        cell4.setVerticalAlignment(VerticalAlignment.MIDDLE);
        XDDFTextParagraph para4 = cell4.getTextBody().getParagraphs().getFirst();
        para4.setTextAlignment(TextAlignment.RIGHT);

        // Percentage
        XSLFTableCell cell5 = table.getCell(row, 5);
        double percentage = total != 0 ? (amount / total) * 100 : 0.0;
        cell5.setText(formatter.format(percentage));
        cell5.setFillColor(bgColor);
        cell5.setVerticalAlignment(VerticalAlignment.MIDDLE);
        XDDFTextParagraph para5 = cell5.getTextBody().getParagraphs().getFirst();
        para5.setTextAlignment(TextAlignment.RIGHT);
    }
}
