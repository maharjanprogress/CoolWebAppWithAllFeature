package com.progress.coolProject.DTO.Excel;


import com.progress.coolProject.Enums.LoanCategory;
import com.progress.coolProject.Enums.LoanPayerCategory;
import com.progress.coolProject.Utils.date.NepaliDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanAccountAgeingDTO {
    private String accountNo;
    private String description;
    private LoanCategory accountName;
    private String period;
    private NepaliDate openingDate;
    private NepaliDate maturedDate;
    private NepaliDate lastRepaymentDate;
    private NepaliDate currentDate;
    private Integer lapsedDays;
    private Double loanAmount;
    private Double balanceAmount;
    private LoanPayerCategory paymentType;

    /**
     * Calculate and set payment type based on lapsed days
     * Call this after setting lapsedDays
     */
    public void calculatePaymentType() {
        if (lapsedDays == null) {
            this.paymentType = null;
            return;
        }

        if (lapsedDays < 0) {
            this.paymentType = LoanPayerCategory.BELOW_ONE;
        } else if (lapsedDays <= 30) {
            this.paymentType = LoanPayerCategory.UPTO_ONE_MONTH;
        } else if (lapsedDays <= 90) {
            this.paymentType = LoanPayerCategory.ONE_TO_THREE_MONTHS;
        } else if (lapsedDays <= 180) {
            this.paymentType = LoanPayerCategory.THREE_TO_SIX_MONTHS;
        } else if (lapsedDays <= 270) {
            this.paymentType = LoanPayerCategory.SIX_TO_NINE_MONTHS;
        } else if (lapsedDays <= 365) {
            this.paymentType = LoanPayerCategory.NINE_TO_TWELVE_MONTHS;
        } else if (lapsedDays <= 730) {
            this.paymentType = LoanPayerCategory.ONE_TO_TWO_YEARS;
        } else {
            this.paymentType = LoanPayerCategory.ABOVE_TWO_YEARS;
        }
    }

    // Validation method
    public boolean isValid() {
        return accountNo != null && !accountNo.trim().isEmpty()
                && accountName != null
                && loanAmount != null && loanAmount > 0;
    }

    public String getValidationError() {
        if (accountNo == null || accountNo.trim().isEmpty()) {
            return "Account number is required";
        }
        if (accountName == null) {
            return "Invalid loan type";
        }
        if (loanAmount == null || loanAmount <= 0) {
            return "Valid loan amount is required";
        }
        return null;
    }
}