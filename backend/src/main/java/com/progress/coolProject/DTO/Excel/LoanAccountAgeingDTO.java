package com.progress.coolProject.DTO.Excel;


import com.progress.coolProject.Enums.LoanCategory;
import com.progress.coolProject.Enums.LoanPayerCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanAccountAgeingDTO {
    private String accountNo;
    private String description;
    private LoanCategory accountName;
    private String period;
    private LocalDate openingDate;
    private LocalDate maturedDate;
    private LocalDate lastRepaymentDate;
    private LocalDate currentDate;
    private Integer lapsedDays;
    private Double loanAmount;
    private Double balanceAmount;
    private LoanPayerCategory paymentType;

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