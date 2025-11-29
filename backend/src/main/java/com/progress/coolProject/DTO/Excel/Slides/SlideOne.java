package com.progress.coolProject.DTO.Excel.Slides;

import com.progress.coolProject.DTO.Excel.ExcelRowDTO;
import com.progress.coolProject.Enums.TrialBalanceEnum;
import com.progress.coolProject.Utils.Excel.ExcelTrialBalanceExcelRowHelper;
import lombok.experimental.UtilityClass;

import java.util.Map;

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
}
